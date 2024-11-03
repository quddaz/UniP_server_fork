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

    @Override
    public List<PartyResponseDto> getMainPartyPage(PartyType partyType) {
        QParty party = QParty.party;
        QMember member = QMember.member;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(party.endTime.goe(LocalDateTime.now())); // 현재 시간 이후의 파티만 조회
        builder.and(party.isClosed.eq(false)); // isClosed가 false인 파티만 조회

        // 파티 타입이 null이 아니면 해당 조건을 추가
        if (partyType != null) {
            builder.and(party.partyType.eq(partyType));
        }

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
            .where(builder)
            .fetch();
    }

    @Override
    public Optional<PartyDetailDto> findPartyDetailById(Long id) {
        QParty party = QParty.party;
        QCourse course = QCourse.course;

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
                course.name,
                course.content
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

    @Override
    public List<PartyMyDto> getMyParty(Long id) {
        QParty party = QParty.party;
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

}
