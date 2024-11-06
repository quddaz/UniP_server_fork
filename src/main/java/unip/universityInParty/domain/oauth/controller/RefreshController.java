package unip.universityInParty.domain.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        @RequestBody RefreshTokenRequest refresh) throws IOException {

        return ResponseEntity.ok(ResponseDto.of("Refresh 재발급 성공", refreshService.refreshAccessToken(refresh.refreshToken())));
    }
    @GetMapping("/refresh")
    @Operation(summary = "테스트용 토큰 검색", description = "모든 토큰을 꺼내옴")
    public ResponseEntity<?> getToken() {
        return ResponseEntity.ok(ResponseDto.of("Refresh", refreshService.get()));
    }
}
