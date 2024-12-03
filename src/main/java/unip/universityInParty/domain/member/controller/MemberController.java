package unip.universityInParty.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unip.universityInParty.domain.member.service.MemberService;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.global.baseResponse.ResponseDto;


@Tag(name = "회원", description = "회원 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/my")
    @Operation(summary = "내 로그인 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.class)))
    public ResponseEntity<?> getMyLogin(@AuthenticationPrincipal AuthMember authMember) {
        return ResponseEntity.ok().body(ResponseDto.of(
            "로그인 정보 조회 성공",
            authMember
        ));
    }

    @PostMapping("/img")
    @Operation(summary = "프로필 이미지 변경", description = "이미지 변경")
    public ResponseEntity<?> setImage(@RequestParam("file") MultipartFile multipartFile,
                                      @AuthenticationPrincipal AuthMember authMember) {
        memberService.setMemberProfileImage(multipartFile, authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "프로필 이미지 변경 성공",
            authMember
        ));
    }

    @PostMapping("/name")
    @Operation(summary = "이름 변경", description = "이름 변경")
    public ResponseEntity<?> setName(@RequestBody String name,
                                     @AuthenticationPrincipal AuthMember authMember) {
        memberService.setMemberName(name, authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.of(
            "이름 변경 성공",
            authMember
        ));
    }
}