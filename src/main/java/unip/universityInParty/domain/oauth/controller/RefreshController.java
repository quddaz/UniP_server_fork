package unip.universityInParty.domain.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.oauth.dto.request.RefreshTokenRequest;
import unip.universityInParty.domain.oauth.refresh.service.RefreshService;
import unip.universityInParty.global.baseResponse.ResponseDto;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Refresh", description = "토큰 재발급 API")
@RequestMapping("/auth")
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    public ResponseEntity<?> reIssueToken(
        @RequestBody RefreshTokenRequest refresh) {

        return ResponseEntity.ok(ResponseDto.of("Refresh 재발급 성공", refreshService.refreshAccessToken(refresh.refreshToken())));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 위해 저장소의 Refresh Token 삭제")
    public ResponseEntity<?> logoutDeleteToken(@AuthenticationPrincipal AuthMember authMember) {
        refreshService.deleteById(authMember.getId());
        return ResponseEntity.ok().body(ResponseDto.ok());
    }

    @GetMapping("/{id}")
    @Operation(summary = "엑세스 받아오기", description = "")
    public ResponseEntity<?> logoutDeleteToken(@RequestParam Long id) {
        String accessToken = refreshService.getAccessToken(id);
        return ResponseEntity.ok().body(ResponseDto.of("조회 성공", accessToken));
    }
}
