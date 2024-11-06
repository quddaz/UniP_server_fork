package unip.universityInParty.global.baseResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    private int code;
    private String message;
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String timestamp;

    public static <T> ResponseDto<T> of(String message, T data) {
        return new ResponseDto<>(HttpStatus.OK.value(), message, data, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public static <T> ResponseDto<T> fail(Integer status, String message) {
        return new ResponseDto<>(status, message, null, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    public static <T> ResponseDto<T> fail_OAuth() {
        return new ResponseDto<>(401, "로그인을 해주세요", null, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    public static <T> ResponseDto<T> fail(Integer status, String message, T data) {
        return new ResponseDto<>(status, message, data, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
