package unip.universityInParty.domain.friend.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.service.FriendService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "친구 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청: 유효하지 않은 회원 ID"),
        @ApiResponse(responseCode = "401", description = "인증 실패: 요청을 수행할 권한이 없습니다.")
    })
    public ResponseEntity<?> acceptFriend(
        @RequestBody Long fromMemberId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        friendService.acceptRequest(fromMemberId, customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 추가 성공", null));
    }

    @DeleteMapping
    @Operation(summary = "친구 삭제", description = "주어진 회원 ID에 대한 친구를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "친구 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청: 유효하지 않은 회원 ID"),
        @ApiResponse(responseCode = "401", description = "인증 실패: 요청을 수행할 권한이 없습니다.")
    })
    public ResponseEntity<?> deleteFriend(
        @RequestBody Long fromMemberId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        friendService.deleteRequest(fromMemberId, customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 삭제 성공", null));
    }

    @GetMapping
    @Operation(summary = "친구 조회", description = "사용자의 친구 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "친구 조회 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(type = "array", implementation = FriendDTO.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패: 요청을 수행할 권한이 없습니다.")
    })
    public ResponseEntity<?> getFriend(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<FriendDTO> friends = friendService.getMyFriend(customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of("친구 조회 성공", friends));
    }
}
