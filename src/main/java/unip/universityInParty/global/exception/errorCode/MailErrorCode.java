package unip.universityInParty.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import unip.universityInParty.global.exception.errorCode.base.ErrorCode;

@AllArgsConstructor
@Getter
public enum MailErrorCode implements ErrorCode {
    FAILED_MAIL_SEND(HttpStatus.EXPECTATION_FAILED, "메일 전송 실패");
    private HttpStatus httpStatus;
    private String message;
}
