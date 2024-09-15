package unip.universityInParty.domain.party.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyDto {

    @NotEmpty(message = "타이틀을 작성해야합니다.")
    private String title;

    @NotEmpty(message = "내용을 작성해야합니다.")
    private String content;

    @Min(value = 2, message = "최대 인원 수는 2 이상입니다.")  // 1 이상의 값만 허용
    private int limit;

    @NotNull(message = "시작 시간을 설정해야 합니다.")
    @FutureOrPresent(message = "")  // 현재 또는 미래의 시간만 허용
    private LocalDateTime startTime;

    @NotNull(message = "끝나는 시간을 설정해야 합니다.")
    @Future(message = "")  // 미래의 시간만 허용
    private LocalDateTime endTime;
}