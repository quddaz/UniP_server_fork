package unip.universityInParty.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import unip.universityInParty.global.exception.errorCode.base.ErrorCode;

@AllArgsConstructor
@Getter
public enum OAuthErrorCode implements ErrorCode {
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다.");
    private HttpStatus httpStatus;
    private String message;
}
