package unip.universityInParty.domain.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.alarm.dto.request.AlarmFriendRequestDTO;
import unip.universityInParty.domain.alarm.dto.request.AlarmInvitationRequestDTO;
import unip.universityInParty.domain.alarm.dto.request.AlarmRequestDTO;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;
import unip.universityInParty.domain.alarm.service.AlarmService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    @PostMapping("/friend")
    public ResponseEntity<?> createFriendRequest(@RequestBody AlarmFriendRequestDTO alarmFriendRequestDTO,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        alarmService.sendFriendRequestAlarm(alarmFriendRequestDTO.getReceiver(), customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 알람 생성 성공", null));
    }
    @PostMapping("/invitation")
    public ResponseEntity<?> createInvitationRequest(@RequestBody AlarmInvitationRequestDTO alarmInvitationRequestDTO,
                                                 @AuthenticationPrincipal CustomUserDetails customUserDetails){
        alarmService.sendInvitationAlarm(alarmInvitationRequestDTO.getReceiver(), customUserDetails.getId(), alarmInvitationRequestDTO.getParty());
        return ResponseEntity.ok().body(ResponseDto.of("초대 알람 생성 성공", null));
    }
    @GetMapping
    public ResponseEntity<?> getAlarm(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<AlarmResponseDTO> alarms = alarmService.retrieveMyAlarms(customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of("알람 조회 성공", alarms));
    }
    @PostMapping("/ok")
    public ResponseEntity<?> okRequest(@RequestBody AlarmRequestDTO alarmRequestDTO){
        alarmService.processAlarmRequest(alarmRequestDTO.getId());
        return ResponseEntity.ok().body(ResponseDto.of("알람 수락 성공", null));
    }
    @PostMapping("/no")
    public ResponseEntity<?> noRequest(@RequestBody AlarmRequestDTO alarmRequestDTO){
        alarmService.processNoAlarmRequest(alarmRequestDTO.getId());
        return ResponseEntity.ok().body(ResponseDto.of("알람 거절 성공", null));
    }
}
