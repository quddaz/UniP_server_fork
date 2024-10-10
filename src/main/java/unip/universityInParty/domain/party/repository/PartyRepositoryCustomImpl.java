package unip.universityInParty.domain.party.repository;

import com.querydsl.core.FetchableQuery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.entity.QFriend;
import unip.universityInParty.domain.member.entity.QMember;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;
import unip.universityInParty.domain.party.entity.QParty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PartyResponseDto> getMainPartyPage() {
        QParty party = QParty.party;
        QMember member = QMember.member;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
            .select(Projections.constructor(PartyResponseDto.class,
                member.name,
                member.profile_image,
                party.title,
                party.partyLimit,
                party.peopleCount,
                party.startTime,
                party.endTime
                ))
            .from(party)
            .join(party.member, member)
            .where(party.endTime.goe(LocalDateTime.now())) // 예시: 현재 시간 이후의 파티만 조회
            .fetch();
    }

    @Override
    public Optional<PartyDetailDto> findPartyDetailById(Long id) {
        QParty party = QParty.party;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        PartyDetailDto result = queryFactory
            .select(Projections.constructor(PartyDetailDto.class,
                party.id,
                party.title,
                party.content,
                party.partyLimit,
                party.peopleCount,
                party.startTime,
                party.endTime
            ))
            .from(party)
            .where(party.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}
