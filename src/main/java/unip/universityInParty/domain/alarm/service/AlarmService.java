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
import unip.universityInParty.domain.party.entity.Party;
import unip.universityInParty.domain.party.service.PartyService;
import unip.universityInParty.domain.pmList.entity.Enum.PartyRole;
import unip.universityInParty.domain.pmList.service.PMListService;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.alarm.exception.AlarmErrorCode;
import unip.universityInParty.domain.party.exception.PartyErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final AlarmDetailRepository alarmDetailRepository;
    private final FriendService friendService;
    private final PMListService pmListService;
    private final PartyService partyService;

    // 알림의 중복 여부를 확인
    private void validateUniqueAlarm(Long receiverId, Long senderId, AlarmCategory category) {
        if (alarmRepository.existsByReceiverAndSenderAndAlarmCategory(receiverId, senderId, category)) {
            throw new CustomException(AlarmErrorCode.ALREADY_EXISTS);
        }
    }

    // 친구 추가 알림 생성
    @Transactional
    public void sendFriendRequestAlarm(Long receiverId, Long senderId) {
        // 친구 요청 알림 전송
        validateUniqueAlarm(receiverId, senderId, AlarmCategory.FRIEND_REQUEST);

        Alarm alarm = Alarm.builder()
            .receiver(receiverId)
            .sender(senderId)
            .alarmCategory(AlarmCategory.FRIEND_REQUEST)
            .build();
        alarmRepository.save(alarm);
    }

    // 초대 알림 생성
    @Transactional
    public void sendInvitationAlarm(Long receiverId, Long senderId, Long partyId) {
        // 초대 알림 전송
        validateUniqueAlarm(receiverId, senderId, AlarmCategory.INVITATION);
        Party party = partyService.getPartyById(partyId);

        if(party.isClosed()){
            throw new CustomException(PartyErrorCode.PARTY_CLOSED);
        }

        Alarm alarm = Alarm.builder()
            .receiver(receiverId)
            .sender(senderId)
            .alarmCategory(AlarmCategory.INVITATION)
            .build();
        alarmRepository.save(alarm); // 알림 먼저 저장

        AlarmDetail alarmDetail = AlarmDetail.builder()
            .alarm(alarm)
            .party(party)
            .build();
        alarmDetailRepository.save(alarmDetail);
    }

    // 내 알림 목록 조회
    public List<AlarmResponseDTO> retrieveMyAlarms(Long receiverId) {
        return alarmRepository.findAlarmsByReceiverId(receiverId);
    }

    // 알람 요청 처리
    @Transactional
    public void processAlarmRequest(Long alarmId) {
        // 알림 요청 처리
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
        // 친구 요청 수락
        friendService.acceptRequest(alarm.getSender(), alarm.getReceiver());
        deleteAlarmAndDetail(alarm);
    }

    // 알림과 알림 세부정보 삭제
    private void deleteAlarmAndDetail(Alarm alarm) {
        alarmDetailRepository.deleteByAlarm(alarm);
        alarmRepository.delete(alarm);
    }


    // 알람 거절
    @Transactional
    public void processNoAlarmRequest(Long alarmId) {
        // 알림 요청이 없을 경우 처리
        Alarm alarm = alarmRepository.findById(alarmId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        if (alarm.getAlarmCategory().equals(AlarmCategory.INVITATION)) {
            handleInvitationAlarm(alarm);
        } else {
            alarmRepository.delete(alarm);
        }
    }

    public void handleInvitationAlarm(Alarm alarm) {
        // 초대 알림 처리
        AlarmDetail alarmDetail = alarmDetailRepository.findByAlarm(alarm)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_DETAIL_NOT_FOUND));

        pmListService.createJoinParty(PartyRole.USER, alarm.getReceiver(), alarmDetail.getParty().getId());
        deleteAlarmAndDetail(alarm);
    }
}
