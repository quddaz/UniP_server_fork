package unip.universityInParty.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.security.custom.CustomUserDetails;


@Tag(name = "회원", description = "회원 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/my")
    @Operation(summary = "내 로그인 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    public ResponseEntity<?> getMyLogin(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok().body(ResponseDto.of(
            "로그인 정보 조회 성공",
            customUserDetails.getMemberDTO()
        ));
    }
}