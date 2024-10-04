package unip.universityInParty.domain.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record CourseDto(
    @NotEmpty(message = "주소를 작성해야합니다.")
    @Schema(description = "코스의 주소", example = "서울특별시 중구 세종대로 110")
    String address,

    @NotEmpty(message = "타이틀을 작성해야합니다.")
    @Schema(description = "코스의 타이틀", example = "서울 술집")
    String title
) {
}