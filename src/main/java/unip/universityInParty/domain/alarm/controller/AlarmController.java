package unip.universityInParty.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.alarm.dto.request.AlarmFriendRequestDTO;
import unip.universityInParty.domain.alarm.dto.request.AlarmInvitationRequestDTO;
import unip.universityInParty.domain.alarm.dto.request.AlarmRequestDTO;
import unip.universityInParty.domain.alarm.dto.response.AlarmResponseDTO;
import unip.universityInParty.domain.alarm.service.AlarmService;
import unip.universityInParty.global.baseResponse.ResponseDto;

import java.util.List;


@Tag(name = "알람", description = "알람 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/friend")
    @Operation(summary = "친구 추가 알람 생성", description = "친구 추가 요청에 대한 알람을 생성합니다.")
    public ResponseEntity<?> createFriendRequest(
        @RequestBody AlarmFriendRequestDTO alarmFriendRequestDTO,
        @AuthenticationPrincipal AuthMember authMember) {
        alarmService.sendFriendRequestAlarm(alarmFriendRequestDTO.receiver(), authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @PostMapping("/invitation")
    @Operation(summary = "초대 알람 생성", description = "초대 요청에 대한 알람을 생성합니다.")
    public ResponseEntity<?> createInvitationRequest(
        @RequestBody AlarmInvitationRequestDTO alarmInvitationRequestDTO,
        @AuthenticationPrincipal AuthMember authMember) {
        alarmService.sendInvitationAlarm(alarmInvitationRequestDTO.receiver(), authMember.getId(), alarmInvitationRequestDTO.party());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @GetMapping
    @Operation(summary = "알람 조회", description = "사용자의 알람 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "알람 조회 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(type = "array", implementation = AlarmResponseDTO.class)))
    public ResponseEntity<?> getAlarm(@AuthenticationPrincipal AuthMember authMember) {
        List<AlarmResponseDTO> alarms = alarmService.retrieveMyAlarms(authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of("알람 조회 성공", alarms));
    }

    @PostMapping("/ok")
    @Operation(summary = "알람 수락", description = "주어진 알람 요청을 수락합니다.")
    public ResponseEntity<?> okRequest(@RequestBody AlarmRequestDTO alarmRequestDTO) {
        alarmService.processAlarmRequest(alarmRequestDTO.id());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @PostMapping("/no")
    @Operation(summary = "알람 거절", description = "주어진 알람 요청을 거절합니다.")
    public ResponseEntity<?> noRequest(@RequestBody AlarmRequestDTO alarmRequestDTO) {
        alarmService.processNoAlarmRequest(alarmRequestDTO.id());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }
}
