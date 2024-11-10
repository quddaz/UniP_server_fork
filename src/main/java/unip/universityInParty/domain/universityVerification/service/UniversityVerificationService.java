package unip.universityInParty.domain.universityVerification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.universityVerification.entity.EmailBlackList;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;
import unip.universityInParty.domain.universityVerification.repository.EmailBlackListRepository;
import unip.universityInParty.domain.universityVerification.repository.UniversityVerificationRepository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.universityVerification.exception.MailErrorCode;
import unip.universityInParty.domain.member.exception.MemberErrorCode;
import java.util.Random;
@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityVerificationService {
    private final JavaMailSender javaMailSender;
    private final UniversityVerificationRepository universityVerificationRepository;

    @Value("${spring.mail.username}")
    private String serviceName;

    private final MemberRepository memberRepository;
    private final EmailBlackListRepository emailBlackListRepository;

    // 6자리 난수 생성
    private String makeRandomNum() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(r.nextInt(10));
        }
        return randomNumber.toString();
    }

    /* 이메일 전송 */
    public void mailSend(String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(serviceName); // 발신자 이메일 설정
            message.setRecipients(MimeMessage.RecipientType.TO, toMail); // 수신자 이메일 설정
            message.setSubject(title); // 이메일 제목 설정

            // 이메일 본문 설정 (HTML 형식)
            message.setText(content, "UTF-8", "html");

            // 이메일 전송
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", toMail, e.getMessage());
            throw new CustomException(MailErrorCode.FAILED_MAIL_SEND);
        }
    }

    /* 이메일 작성 및 인증 코드 저장 */
    public void sendVerificationEmail(String email) {
        if(emailBlackListRepository.existsByEmail(email)){
            throw new CustomException(MailErrorCode.ALREADY_EMAIL);
        }
        String authNumber = makeRandomNum(); // 난수 생성
        String title = "회원 가입을 위한 이메일입니다!";
        String content = "이메일을 인증하기 위한 절차입니다." +
            "<br><br>" +
            "인증 번호는 " + authNumber + "입니다." +
            "<br>" +
            "회원 가입 폼에 해당 번호를 입력해주세요.";

        mailSend(email, title, content);

        // Redis에 인증 코드 저장
        UniversityVerification verification = UniversityVerification.builder()
            .email(email)
            .authCode(authNumber)
            .expiration(180L)
            .build();
        universityVerificationRepository.save(verification); // Redis에 저장
    }


    /* 인증 코드 검증 메서드 */
    public void verifyAuthCode(String email, String code, Long id) {
        UniversityVerification verification = universityVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(MemberErrorCode.UNIVERSITY_VERIFICATION_NOT_FOUND));
        log.info("Code = {} , AuthCode = {}", code, verification.getAuthCode());
        if (verification.getAuthCode().equals(code)) {
            Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
            member.setAuth(true);
            memberRepository.save(member);

            EmailBlackList emailBlackList = EmailBlackList.builder()
                .email(email).build();
            emailBlackListRepository.save(emailBlackList);
        } else {
            throw new CustomException(MemberErrorCode.INVALID_AUTH_CODE);
        }
    }
}

