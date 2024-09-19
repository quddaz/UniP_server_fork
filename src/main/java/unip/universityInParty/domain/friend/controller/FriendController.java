package unip.universityInParty.domain.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.friend.dto.FriendDTO;
import unip.universityInParty.domain.friend.service.FriendService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;
    @PostMapping
    public ResponseEntity<?> acceptFriend(@RequestBody Long fromMemberId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        friendService.acceptRequest(fromMemberId, customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "친구 추가 성공",
            null
        ));
    }
    @DeleteMapping
    public ResponseEntity<?> deleteFriend(@RequestBody Long fromMemberId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        friendService.deleteRequest(fromMemberId, customUserDetails.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "친구 삭제 성공",
            null
        ));
    }
    @GetMapping
    public ResponseEntity<?> getFriend(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok().body(ResponseDto.of(
            "친구 조회 성공",
            friendService.getMyFriend(customUserDetails.getId())
        ));
    }
}
