package unip.universityInParty.domain.refresh.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import unip.universityInParty.domain.refresh.service.RefreshService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.OAuthErrorCode;


import java.util.HashMap;
import java.util.Map;
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Refresh", description = "토큰 재발급 API")
public class RefreshController {
    private final RefreshService refreshService;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Refresh 재발급 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("리프레쉬 재발급");

        // 쿠키에서 Refresh 토큰 얻기
        String refresh = getRefreshTokenFromCookies(request);
        if (refresh == null) {
            throw new CustomException(OAuthErrorCode.REFRESH_TOKEN_NULL);
        }

        Map<String, String> responseBody = refreshService.refreshAccessToken(refresh, response);
        return ResponseEntity.ok(ResponseDto.of("Refresh 재발급 성공", responseBody));
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
