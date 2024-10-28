package unip.universityInParty.domain.party.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import unip.universityInParty.domain.course.dto.CourseDto;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import unip.universityInParty.domain.party.entity.type.PartyType;

@Builder
@Schema(description = "파티 정보 DTO")
public record PartyDto(
    @NotEmpty(message = "타이틀을 작성해야합니다.")
    @Schema(description = "파티 제목", example = "여름 바비큐 파티")
    String title,

    @NotEmpty(message = "내용을 작성해야합니다.")
    @Schema(description = "파티 내용", example = "여름 바비큐 파티에 초대합니다!")
    String content,

    @Min(value = 2, message = "최대 인원 수는 2 이상입니다.")
    @Schema(description = "최대 인원 수", example = "10")
    int limit,

    @Schema(description = "파티 타입", example = "RESTAURANT,BAR,COMPREHENSIVE")
    PartyType partyType,
    @NotNull(message = "시작 시간을 설정해야 합니다.")
    @FutureOrPresent(message = "미래의 시간이여야 합니다")
    @Schema(description = "시작 시간", example = "2024-10-10T10:00:00")
    LocalDateTime startTime,

    @NotNull(message = "끝나는 시간을 설정해야 합니다.")
    @Future(message = "미래의 시간이여야 합니다.")
    @Schema(description = "끝나는 시간", example = "2024-10-10T15:00:00")
    LocalDateTime endTime,

    @NotNull(message = "코스 목록이 필요합니다.")
    @Schema(description = "코스 목록", implementation = CourseDto.class)
    List<CourseDto> courses
) {
    public static PartyDto toPartyDto(PartyGptDto partyGptDto){
        List<CourseDto> courseDtoList = CourseDto.toCourseDto(partyGptDto.courses());
        return PartyDto.builder()
            .title(partyGptDto.title())
            .content(partyGptDto.content())
            .limit(partyGptDto.limit())
            .startTime(partyGptDto.startTime())
            .partyType(PartyType.COMPREHENSIVE)
            .endTime(partyGptDto.endTime())
            .courses(courseDtoList)
            .build();
    }
}
