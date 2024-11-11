package unip.universityInParty.domain.universityVerification.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import unip.universityInParty.domain.universityVerification.exception.MailErrorCode;
import unip.universityInParty.global.exception.custom.CustomException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {
    @Value("${spring.mail.username}")
    private String serviceName;

    private final JavaMailSender javaMailSender;
    private final String DEFAULT_SUBJECT = "UniP 학교 인증 메일";
    private final String DEFAULT_CONTENT =
        "이메일을 인증하기 위한 절차입니다." +
        "<br><br>" +
        "회원 가입 폼에 해당 번호를 입력해주세요." + "<br>"
        +"인증번호:";

    /* 이메일 전송 */
    @Async("emailAsyncExecutor")
    public void sendEmail(String toMail, String authNumber) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(serviceName); // 발신자 이메일 설정
            helper.setTo(toMail);        // 수신자 이메일 설정
            helper.setSubject(DEFAULT_SUBJECT);  // 이메일 제목 설정
            helper.setText(DEFAULT_CONTENT + authNumber , true); // 이메일 본문 설정 (HTML 형식)

            javaMailSender.send(message);
            log.info("send email to: {}", toMail);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", toMail, e.getMessage());
            throw new CustomException(MailErrorCode.FAILED_MAIL_SEND);
        }
    }
}
