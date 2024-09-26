package unip.universityInParty.global.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import unip.universityInParty.global.exception.errorCode.base.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
}