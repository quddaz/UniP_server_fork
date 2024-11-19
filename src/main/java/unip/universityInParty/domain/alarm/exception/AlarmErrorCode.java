package unip.universityInParty.domain.alarm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import unip.universityInParty.global.exception.errorCode.ErrorCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlarmErrorCode implements ErrorCode {
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 친구 요청 알림입니다."),
    ALARM_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 알람입니다."),
    ALARM_DETAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 초대 알람입니다.");
    private HttpStatus httpStatus;
    private String message;
}
