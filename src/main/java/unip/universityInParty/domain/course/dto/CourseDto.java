package unip.universityInParty.domain.course.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    @NotEmpty(message = "주소를 작성해야합니다.")
    private String address;
    @NotEmpty(message = "타이틀을 작성해야합니다.")
    private String title;

}
