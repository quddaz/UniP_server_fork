package unip.universityInParty.domain.alarm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "초대 알림 요청 DTO")
public record AlarmInvitationRequestDTO(
    @Schema(description = "초대할 파티 ID", example = "10")
    Long party,
    @Schema(description = "수신자의 ID", example = "1")
    Long receiver
) {
}