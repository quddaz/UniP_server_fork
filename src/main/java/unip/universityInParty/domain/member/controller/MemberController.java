package unip.universityInParty.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/my")
    public ResponseEntity<?> getMyLogin(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok().body(ResponseDto.of(
            "로그인 정보 조회 성공",
            customUserDetails.getMemberDTO()
        ));
    }
}
