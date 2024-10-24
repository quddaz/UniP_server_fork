package unip.universityInParty.domain.party.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import unip.universityInParty.domain.course.dto.CourseGptDto;
import unip.universityInParty.domain.course.entity.Course;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PartyGptDto (    String title,
                               String content,
                               int limit,
                               LocalDateTime startTime,
                               LocalDateTime endTime,

                            List<CourseGptDto> courses,
                            @JsonProperty("route summary")
                            String routeSummary)
{
}
