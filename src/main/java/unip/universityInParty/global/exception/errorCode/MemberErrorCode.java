package unip.universityInParty.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import unip.universityInParty.global.exception.errorCode.base.ErrorCode;

@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NO_UNI_VERIFICATION_ACCESS(HttpStatus.CONFLICT, "학교 인증이 필요합니다."),
    ALREADY_UNI_VERIFICATION(HttpStatus.BAD_REQUEST, "이미 학교인증이 완료되었습니다."),
    UNIVERSITY_VERIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "인증을 찾을 수 없습니다."),
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 다릅니다.");
    private HttpStatus httpStatus;
    private String message;
}
