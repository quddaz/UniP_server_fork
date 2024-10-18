package unip.universityInParty.domain.party.dto.response;


import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import unip.universityInParty.domain.course.dto.CourseDto;

@Builder
@Schema(description = "파티 상세 정보 DTO")
public record PartyDetailDto(
    @Schema(description = "파티 ID", example = "1")
    Long id,

    @Schema(description = "파티 제목", example = "여름 바비큐 파티")
    String title,

    @Schema(description = "파티 내용", example = "여름 바비큐 파티에 초대합니다!")
    String content,

    @Schema(description = "최대 인원 수", example = "10")
    int limit,

    @Schema(description = "현재 인원 수", example = "5")
    int peopleCount,

    @Schema(description = "시작 시간", example = "2024-10-10T10:00:00")
    LocalDateTime startTime,

    @Schema(description = "끝나는 시간", example = "2024-10-10T15:00:00")
    LocalDateTime endTime,
    @Schema(description = "코스리스트", example = "[{\"address\": \"서울특별시 중구 세종대로 110\", \"title\": \"서울 술집\"}]")
    List<CourseDto> courses
) {
    public PartyDetailDto withCourses(List<CourseDto> courses) {
        return new PartyDetailDto(id, title, content, limit, peopleCount, startTime, endTime, courses);
    }
}
