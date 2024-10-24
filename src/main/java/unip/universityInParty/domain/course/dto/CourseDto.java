package unip.universityInParty.domain.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public record CourseDto(
    @NotEmpty(message = "주소를 작성해야합니다.")
    String address,

    @NotEmpty(message = "이름을 작성해야합니다.")
    String name,
    @NotEmpty(message = "내용을 작성해야합니다.")
    String content
) {
    // CourseGptDto 리스트를 CourseDto 리스트로 변환하는 메서드
    public static List<CourseDto> toCourseDto(List<CourseGptDto> courseGptDtos) {
        if (courseGptDtos == null) {
            return Collections.emptyList();
        }

        return courseGptDtos.stream()
            .map(courseGptDto -> CourseDto.builder()
                .address(courseGptDto.address())
                .name(courseGptDto.name())
                .content(courseGptDto.content())
                .build())
            .toList();
    }
}