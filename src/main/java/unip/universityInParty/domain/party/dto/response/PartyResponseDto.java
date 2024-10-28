package unip.universityInParty.domain.party.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import unip.universityInParty.domain.party.entity.type.PartyType;

import java.time.LocalDateTime;
@Builder
@Schema(description = "파티 정보 DTO")
public record PartyResponseDto(
    @Schema(description = "파티장의 이름", example = "홍길동")
    String name,

    @Schema(description = "파티장의 프로필 이미지 URL", example = "http://example.com/profile.jpg")
    String profile_image,

    @Schema(description = "파티 제목", example = "여름 바비큐 파티")
    String title,
    @Schema(description = "파티 타입", example = "RESTAURANT,BAR,COMPREHENSIVE")
    PartyType partyType,

    @Schema(description = "최대 인원 수", example = "10")
    int limit,

    @Schema(description = "현재 인원 수", example = "5")
    int peopleCount,

    @Schema(description = "시작 시간", example = "2024-10-10T10:00:00")
    LocalDateTime startTime,

    @Schema(description = "끝나는 시간", example = "2024-10-10T15:00:00")
    LocalDateTime endTime
) {
}
