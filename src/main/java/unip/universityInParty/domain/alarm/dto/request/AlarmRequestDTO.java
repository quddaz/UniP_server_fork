package unip.universityInParty.domain.alarm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알림 수락/거절 요청 DTO")
public record AlarmRequestDTO(
    @Schema(description = "알림 ID", example = "1")
    Long id
) {
}