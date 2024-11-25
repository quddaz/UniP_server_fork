package unip.universityInParty.domain.friend.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.service.FriendService;
import unip.universityInParty.global.baseResponse.ResponseDto;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "친구", description = "친구 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping
    @Operation(summary = "친구 요청 수락", description = "주어진 회원 ID에 대한 친구 요청을 수락합니다.")
    public ResponseEntity<?> acceptFriend(
        @RequestBody Long fromMemberId,
        @AuthenticationPrincipal AuthMember authMember) {
        friendService.acceptRequest(fromMemberId, authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @DeleteMapping
    @Operation(summary = "친구 삭제", description = "주어진 회원 ID에 대한 친구를 삭제합니다.")
    public ResponseEntity<?> deleteFriend(
        @RequestBody Long fromMemberId,
        @AuthenticationPrincipal AuthMember authMember) {
        friendService.deleteRequest(fromMemberId, authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @GetMapping
    @Operation(summary = "친구 조회", description = "사용자의 친구 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "친구 조회 성공", content = @Content(mediaType = "application/json",
        schema = @Schema(type = "array", implementation = FriendDTO.class)))
    public ResponseEntity<?> getFriend(@AuthenticationPrincipal AuthMember authMember) {
        List<FriendDTO> friends = friendService.getMyFriend(authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 조회 성공", friends));
    }

    @GetMapping("/bored")
    @Operation(summary = "메인 페이지 친구 조회", description = "한가한 친구 최대 5명 조회")
    @ApiResponse(responseCode = "200", description = "친구 조회 성공", content = @Content(mediaType = "application/json",
        schema = @Schema(type = "array", implementation = FriendDTO.class)))
    public ResponseEntity<?> getBoredFriend(@AuthenticationPrincipal AuthMember authMember) {
        List<FriendDTO> friends = friendService.getBoredFriend(authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 조회 성공", friends));
    }
}
