package unip.universityInParty.domain.party.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.course.dto.CourseDto;
import unip.universityInParty.domain.course.entity.QCourse;
import unip.universityInParty.domain.member.entity.QMember;
import unip.universityInParty.domain.party.dto.response.PartyDetailDto;
import unip.universityInParty.domain.party.dto.response.PartyMyDto;
import unip.universityInParty.domain.party.dto.response.PartyResponseDto;
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.entity.QParty;
import unip.universityInParty.domain.party.entity.type.PartyType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QParty party = QParty.party;
    private final QMember member = QMember.member;
    private final QCourse course = QCourse.course;

    @Override
    public List<PartyResponseDto> getMainPartyPage(PartyType partyType) {
        BooleanBuilder conditions = createMainPartyConditions(partyType);

        return queryFactory
            .select(Projections.constructor(PartyResponseDto.class,
                party.id,
                member.name,
                member.profile_image,
                party.title,
                party.partyType,
                party.partyLimit,
                party.peopleCount,
                party.startTime,
                party.endTime
            ))
            .from(party)
            .join(party.member, member)
            .where(conditions)
            .fetch();
    }

    @Override
    public Optional<PartyDetailDto> findPartyDetailById(Long id) {
        Party result = queryFactory
            .selectFrom(party)
            .where(party.id.eq(id))
            .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        List<CourseDto> courses = queryFactory
            .select(Projections.constructor(CourseDto.class,
                course.address,
                course.name,
                course.content
            ))
            .from(course)
            .where(course.party.id.eq(result.getId()))
            .fetch();

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

        return Optional.of(partyDetailDto);
    }

    @Override
    public List<PartyMyDto> getMyParty(Long id) {
        return queryFactory
            .select(Projections.constructor(PartyMyDto.class,
                party.id,
                party.title,
                party.partyLimit,
                party.peopleCount
            ))
            .from(party)
            .where(party.member.id.eq(id))
            .fetch();
    }

    private BooleanBuilder createMainPartyConditions(PartyType partyType) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(party.endTime.goe(LocalDateTime.now())); // 현재 시간 이후의 파티만 조회
        builder.and(party.isClosed.isFalse()); // 종료되지 않은 파티만 조회

        if (partyType != null) {
            builder.and(party.partyType.eq(partyType)); // 파티 타입 조건 추가
        }

        return builder;
    }
}
