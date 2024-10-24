package unip.universityInParty.domain.course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CourseGptDto(
    String name,
    String content,
    String rating,
    String address,
    @JsonProperty("estimated travel time") String estimatedTravelTime
) {
}