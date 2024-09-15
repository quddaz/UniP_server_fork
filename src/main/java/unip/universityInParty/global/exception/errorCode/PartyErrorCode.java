package unip.universityInParty.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PartyErrorCode implements ErrorCode {
    UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "제거 권한이 없습니다."),
    PARTY_NOT_FOUND(HttpStatus.BAD_REQUEST, "파티를 찾을 수 없습니다.");
    private HttpStatus httpStatus;
    private String message;
}
