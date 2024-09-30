package unip.universityInParty.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;
import unip.universityInParty.domain.alarm.entity.Alarm;
import unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory;
import unip.universityInParty.domain.alarm.repository.AlarmRepository;
import unip.universityInParty.domain.alarmDetail.entity.AlarmDetail;
import unip.universityInParty.domain.alarmDetail.repository.AlarmDetailRepository;
import unip.universityInParty.domain.friend.service.FriendService;
import unip.universityInParty.domain.party.service.PartyService;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.AlarmErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final AlarmDetailRepository alarmDetailRepository;
    private final PartyService partyService;
    private final FriendService friendService;

    private void validateUniqueAlarm(Long receiverId, Long senderId, AlarmCategory category) {
        if (alarmRepository.existsByReceiverAndSenderAndAlarmCategory(receiverId, senderId, category)) {
            throw new CustomException(AlarmErrorCode.ALREADY_EXISTS);
        }
    }

    @Transactional
    public void sendFriendRequestAlarm(Long receiverId, Long senderId) {
        validateUniqueAlarm(receiverId, senderId, AlarmCategory.FRIEND_REQUEST);

        Alarm alarm = Alarm.builder()
            .receiver(receiverId)
            .sender(senderId)
            .alarmCategory(AlarmCategory.FRIEND_REQUEST)
            .build();
        alarmRepository.save(alarm);
    }

    @Transactional
    public void sendInvitationAlarm(Long receiverId, Long senderId, Long partyId) {
        validateUniqueAlarm(receiverId, senderId, AlarmCategory.INVITATION);

        Alarm alarm = Alarm.builder()
            .receiver(receiverId)
            .sender(senderId)
            .alarmCategory(AlarmCategory.INVITATION)
            .build();
        alarmRepository.save(alarm); // 알림 먼저 저장

        AlarmDetail alarmDetail = AlarmDetail.builder()
            .alarm(alarm)
            .party(partyId)
            .build();
        alarmDetailRepository.save(alarmDetail);
    }

    public List<AlarmResponseDTO> retrieveMyAlarms(Long receiverId) {
        return alarmRepository.findAlarmsByReceiverId(receiverId);
    }
    @Transactional
    public void processAlarmRequest(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));
        if (alarm.getAlarmCategory().equals(AlarmCategory.FRIEND_REQUEST)) {
            acceptFriendRequest(alarm);
        }
        else if(alarm.getAlarmCategory().equals(AlarmCategory.INVITATION)){
            handleInvitationAlarm(alarm);
        }
    }

    public void acceptFriendRequest(Alarm alarm) {
        friendService.acceptRequest(alarm.getSender(), alarm.getReceiver());
    }

    public void handleInvitationAlarm(Alarm alarm) {
        // 초대 알림 처리 로직 추가
    }
    @Transactional
    public void processNoAlarmRequest(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));
        if(alarm.getAlarmCategory().equals(AlarmCategory.INVITATION)){
            alarmDetailRepository.deleteByAlarm(alarm);
        }
        alarmRepository.delete(alarm);
    }
}
