package unip.universityInParty.domain.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.friend.entity.Friend;
import unip.universityInParty.domain.member.entity.Member;
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    void deleteByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);


}
