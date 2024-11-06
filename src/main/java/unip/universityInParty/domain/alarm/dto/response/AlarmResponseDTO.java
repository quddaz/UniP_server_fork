package unip.universityInParty.domain.alarm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "알림 조회 DTO")
public record AlarmResponseDTO(
    @Schema(description = "알림 ID", example = "1")
    long id,
    @Schema(description = "수신자의 ID", example = "2")
    Long receiver,
    @Schema(description = "알람 카테고리", example = "INVITATION,FRIEND_REQUEST")
    String category,
    @Schema(description = "발신자의 ID", example = "3")
    Long sender,
    @Schema(description = "발신자의 이름", example = "홍길동")
    String senderName,
    @Schema(description = "발신자의 프로필 이미지 URL", example = "https://example.com/image.jpg")
    String senderProfileImage,
    @Schema(description = "파티 제목", example = "생일 파티")
    String partyTitle
) {
}