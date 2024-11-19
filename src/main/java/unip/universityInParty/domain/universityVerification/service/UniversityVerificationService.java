package unip.universityInParty.domain.universityVerification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.universityVerification.entity.EmailBlackList;
import unip.universityInParty.domain.universityVerification.entity.UniversityVerification;
import unip.universityInParty.domain.universityVerification.repository.EmailBlackListRepository;
import unip.universityInParty.domain.universityVerification.repository.UniversityVerificationRepository;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.universityVerification.utils.EmailSender;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.universityVerification.exception.MailErrorCode;
import unip.universityInParty.domain.member.exception.MemberErrorCode;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityVerificationService {
    private final UniversityVerificationRepository universityVerificationRepository;
    private final MemberRepository memberRepository;
    private final EmailBlackListRepository emailBlackListRepository;
    private final EmailSender emailSender;

    // 6자리 난수 생성
    private String makeRandomNum() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(r.nextInt(10));
        }
        return randomNumber.toString();
    }

    /* 이메일 작성 및 인증 코드 저장 */
    public void sendVerificationEmail(String email) {
        if (emailBlackListRepository.existsByEmail(email)) {
            throw new CustomException(MailErrorCode.ALREADY_EMAIL);
        }
        //Redis에서 기존 인증 코드 삭제
        universityVerificationRepository.deleteByEmail(email);

        String authNumber = makeRandomNum(); // 난수 생성

        emailSender.sendEmail(email, authNumber);

        UniversityVerification universityVerification = UniversityVerification.builder()
            .email(email)
            .authCode(authNumber)
            .build();
        universityVerificationRepository.save(universityVerification); // Redis에 저장
    }


    /* 인증 코드 검증 메서드 */
    public void verifyAuthCode(String email, String code, Long memberId) {
        // 이메일로 인증 정보 조회
        UniversityVerification verification = universityVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(MemberErrorCode.UNIVERSITY_VERIFICATION_NOT_FOUND));

        // 인증 코드가 일치하는지 확인
        if (!verification.getAuthCode().equals(code)) {
            throw new CustomException(MemberErrorCode.INVALID_AUTH_CODE);
        }

        // 회원 정보 조회 및 인증 처리
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setAuth(true);
        memberRepository.save(member);

        // 이메일을 블랙리스트에 추가
        emailBlackListRepository.save(new EmailBlackList(email));
    }
}

