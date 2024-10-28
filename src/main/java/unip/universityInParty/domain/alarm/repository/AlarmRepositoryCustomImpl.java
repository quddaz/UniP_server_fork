package unip.universityInParty.domain.alarm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;
import unip.universityInParty.domain.alarm.entity.QAlarm;
import unip.universityInParty.domain.alarmDetail.entity.QAlarmDetail;
import unip.universityInParty.domain.member.entity.QMember;
import unip.universityInParty.domain.party.entity.QParty;

import java.util.List;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<AlarmResponseDTO> findAlarmsByReceiverId(Long receiverId) {
        QAlarm alarm = QAlarm.alarm;
        QAlarmDetail alarmDetail = QAlarmDetail.alarmDetail;
        QMember sender = QMember.member;
        QParty party = QParty.party;

        return queryFactory
            .select(Projections.constructor(AlarmResponseDTO.class,
                alarm.id,
                alarm.receiver,
                alarm.alarmCategory.stringValue(),
                alarm.sender,
                sender.name,
                sender.profile_image,
                party.title
            ))
            .from(alarm)
            .leftJoin(sender).on(alarm.sender.eq(sender.id)) // 발신자 정보
            .leftJoin(alarmDetail).on(alarmDetail.alarm.eq(alarm)) // 알람 세부 정보
            .leftJoin(party).on(alarmDetail.party.eq(party)) // 파티 정보
            .where(alarm.receiver.eq(receiverId)) // 리시버 ID로 필터링
            .fetch();
    }
}
