package unip.universityInParty.domain.alarm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "친구 추가 요청 알림 DTO")
public record AlarmFriendRequestDTO(
    @Schema(description = "알림을 받는 사람의 ID", example = "1")
    Long receiver
) {
}