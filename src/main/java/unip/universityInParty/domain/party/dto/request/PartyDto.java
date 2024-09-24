package unip.universityInParty.domain.party.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unip.universityInParty.domain.course.dto.CourseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyDto {

    @NotEmpty(message = "타이틀을 작성해야합니다.")
    private String title;

    @NotEmpty(message = "내용을 작성해야합니다.")
    private String content;

    @Min(value = 2, message = "최대 인원 수는 2 이상입니다.")
    private int limit;

    @NotNull(message = "시작 시간을 설정해야 합니다.")
    @FutureOrPresent(message = "")
    private LocalDateTime startTime;

    @NotNull(message = "끝나는 시간을 설정해야 합니다.")
    @Future(message = "")
    private LocalDateTime endTime;

    @NotNull(message = "코스 목록이 필요합니다.")
    private List<CourseDto> courses; // CourseDto를 포함하도록 수정
}