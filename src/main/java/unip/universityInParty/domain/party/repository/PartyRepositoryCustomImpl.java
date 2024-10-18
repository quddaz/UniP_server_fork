package unip.universityInParty.domain.party.repository;

import com.querydsl.core.FetchableQuery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.course.dto.CourseDto;
import unip.universityInParty.domain.course.entity.QCourse;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.entity.QFriend;
import unip.universityInParty.domain.member.entity.QMember;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;
import unip.universityInParty.domain.party.entity.Party;
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
            .where(
                party.endTime.goe(LocalDateTime.now()) // 현재 시간 이후의 파티만 조회
                    .and(party.isClosed.eq(false)) // isClosed가 false인 파티만 조회
            )
            .fetch();
    }

    @Override
    public Optional<PartyDetailDto> findPartyDetailById(Long id) {
        QParty party = QParty.party;
        QCourse course = QCourse.course;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // Party 정보 가져오기
        Party result = queryFactory
            .selectFrom(party)
            .where(party.id.eq(id))
            .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        // Course 정보 가져오기
        List<CourseDto> courses = queryFactory
            .select(Projections.constructor(CourseDto.class,
                course.address,
                course.title
            ))
            .from(course)
            .where(course.party.id.eq(result.getId()))
            .fetch();

        // PartyDetailDto 생성
        PartyDetailDto partyDetailDto = PartyDetailDto.builder()
            .id(result.getId())
            .title(result.getTitle())
            .content(result.getContent())
            .limit(result.getPartyLimit())
            .peopleCount(result.getPeopleCount())
            .startTime(result.getStartTime())
            .endTime(result.getEndTime())
            .courses(courses)
            .build();

        return Optional.ofNullable(partyDetailDto);
    }

}
