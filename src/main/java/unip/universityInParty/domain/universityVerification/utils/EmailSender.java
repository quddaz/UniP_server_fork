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

    /* 이메일 전송 */
    @Async
    public void sendEmail(String toMail, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(serviceName); // 발신자 이메일 설정
            helper.setTo(toMail);        // 수신자 이메일 설정
            helper.setSubject(subject);  // 이메일 제목 설정
            helper.setText(content, true); // 이메일 본문 설정 (HTML 형식)

            javaMailSender.send(message);
            log.info("send email to: {}", toMail);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}. Error: {}", toMail, e.getMessage());
            throw new CustomException(MailErrorCode.FAILED_MAIL_SEND);
        }
    }
}
