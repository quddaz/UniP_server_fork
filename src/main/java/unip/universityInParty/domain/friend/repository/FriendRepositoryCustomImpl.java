package unip.universityInParty.domain.friend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.entity.QFriend;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.QMember;

import java.util.List;

import static unip.universityInParty.domain.friend.entity.QFriend.friend;
import static unip.universityInParty.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FriendDTO> getMyFriend(Long id) {


        return queryFactory
            .select(Projections.constructor(FriendDTO.class,
                member.id,
                member.name,
                member.profileImage,
                member.status))
            .from(friend)
            .join(friend.toMember, member)
            .where(friend.fromMember.id.eq(id))
            .fetch();
    }

    @Override
    public List<FriendDTO> getBoredFriend(Long id) {

        return queryFactory
            .select(Projections.constructor(FriendDTO.class,
                member.id,
                member.name,
                member.profileImage,
                member.status))
            .from(friend)
            .join(friend.fromMember, member)
            .where(friend.fromMember.id.eq(id)
                .and(member.status.eq(Status.BORED)))
            .limit(5)  // 최대 5개만 가져오기
            .fetch();
    }
}
