package unip.universityInParty.domain.alarm.repository;

import org.springframework.stereotype.Repository;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;

import java.util.List;

@Repository

public interface AlarmRepositoryCustom {
    public List<AlarmResponseDTO> findAlarmsByReceiverId(Long receiverId);
}
