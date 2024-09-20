package unip.universityInParty.domain.alarmDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.alarm.entity.Alarm;
import unip.universityInParty.domain.alarmDetail.entity.AlarmDetail;

@Repository
public interface AlarmDetailRepository extends JpaRepository<AlarmDetail, Long>{

    void deleteByAlarm(Alarm alarm);
}
