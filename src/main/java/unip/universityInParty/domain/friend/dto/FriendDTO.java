package unip.universityInParty.domain.friend.dto;

import lombok.*;
import unip.universityInParty.domain.member.entity.Enum.Status;

@Builder
public record FriendDTO(
    Long id,
    String name,         // 친구의 이름
    String profile_image, // 프로필 이미지
    Status status) {     // 상태
}
