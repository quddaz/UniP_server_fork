package unip.universityInParty.domain.friend.repository;

import unip.universityInParty.domain.friend.dto.FriendDTO;

import java.util.List;

public interface FriendRepositoryCustom {
    List<FriendDTO> getMyFriend(Long id);

    List<FriendDTO> getBoredFriend(Long id);
}
