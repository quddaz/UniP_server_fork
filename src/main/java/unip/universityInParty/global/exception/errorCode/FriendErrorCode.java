package unip.universityInParty.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import unip.universityInParty.global.exception.errorCode.base.ErrorCode;

@AllArgsConstructor
@Getter
public enum FriendErrorCode implements ErrorCode {
    ALREADY_FRIENDS(HttpStatus.BAD_REQUEST, "이미 친구입니다.");
    private HttpStatus httpStatus;
    private String message;


}
