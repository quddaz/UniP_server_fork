package unip.universityInParty.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.alarm.entity.Alarm;
import unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    boolean existsByReceiverAndSenderAndAlarmCategory(Long receiverId, Long senderId, AlarmCategory category);
}
