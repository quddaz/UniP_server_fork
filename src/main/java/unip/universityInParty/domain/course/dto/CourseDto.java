package unip.universityInParty.domain.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record CourseDto(
    @NotEmpty(message = "주소를 작성해야합니다.")
    String address,

    @NotEmpty(message = "이름을 작성해야합니다.")
    String name,
    @NotEmpty(message = "내용을 작성해야합니다.")
    String content
) {
}