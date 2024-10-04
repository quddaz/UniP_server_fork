package unip.universityInParty.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import unip.universityInParty.domain.member.entity.Enum.Status;

@Builder
public record FriendDTO(
    @Schema(description = "친구 ID", example = "1")
    Long id,

    @Schema(description = "친구의 이름", example = "홍길동")
    String name,

    @Schema(description = "친구의 프로필 이미지 URL", example = "http://example.com/profile.jpg")
    String profile_image,

    @Schema(description = "친구의 상태", example = "BORED")
    Status status
) {
}