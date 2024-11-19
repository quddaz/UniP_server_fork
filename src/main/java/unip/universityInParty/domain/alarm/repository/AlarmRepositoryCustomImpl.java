package unip.universityInParty.domain.alarm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;

import java.util.List;

import static unip.universityInParty.domain.alarm.entity.QAlarm.alarm;
import static unip.universityInParty.domain.alarmDetail.entity.QAlarmDetail.alarmDetail;
import static unip.universityInParty.domain.member.entity.QMember.member;
import static unip.universityInParty.domain.party.entity.QParty.party;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AlarmResponseDTO> findAlarmsByReceiverId(Long receiverId) {
        return queryFactory
            .select(Projections.constructor(AlarmResponseDTO.class,
                alarm.id,
                alarm.receiver,
                alarm.alarmCategory.stringValue(),
                alarm.sender,
                member.name,
                member.profileImage,
                party.title
            ))
            .from(alarm)
            .join(member).on(alarm.sender.eq(member.id)) // 발신자 정보 (INNER JOIN)
            .join(alarmDetail).on(alarmDetail.alarm.eq(alarm)) // 알람 세부 정보 (INNER JOIN)
            .join(party).on(alarmDetail.party.eq(party)) // 파티 정보 (INNER JOIN)
            .where(alarm.receiver.eq(receiverId)) // 리시버 ID로 필터링
            .fetch();
    }

}
