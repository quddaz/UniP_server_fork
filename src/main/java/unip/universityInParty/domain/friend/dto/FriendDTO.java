package unip.universityInParty.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unip.universityInParty.domain.member.entity.Enum.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendDTO {
    private Long id;
    private String name;  // 친구의 이름
    private String profile_image;
    private Status status;
}