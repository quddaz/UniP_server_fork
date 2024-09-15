package unip.universityInParty.domain.refresh.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.refresh.service.RefreshService;
import unip.universityInParty.global.baseResponse.ResponseDto;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.OAuthErrorCode;
import unip.universityInParty.global.security.jwt.JwtUtil;
import unip.universityInParty.global.util.CookieStore;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RefreshController {
    private final JwtUtil jwtUtil;
    private final CookieStore cookieStore;
    private final RefreshService refreshService;
    private final MemberRepository memberRepository;
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("리프레쉬 재발급");
        //쿠키에서 Refresh 토큰 얻기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break; // 필요한 쿠키를 찾았으면 루프 종료
                }
            }
        }

        if (refresh == null) {
            throw new CustomException(OAuthErrorCode.REFRESH_TOKEN_NULL);
        }

        //만료 시간 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(OAuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            throw new CustomException(OAuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshService.existsByRefresh(refresh);
        if (!isExist) {

            throw new CustomException(OAuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refresh);
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        String newAccess= jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()), "access");
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()), "refresh");

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshService.deleteByRefresh(refresh);
        refreshService.addRefresh(username, newRefresh);

        //response
        response.setHeader("access", newAccess);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("name", member.getName());
        responseBody.put("profile", member.getProfile_image());

        response.addCookie(cookieStore.createCookie(newRefresh));
        return ResponseEntity.ok(ResponseDto.of("Refresh 재발급 성공",responseBody));
    }
}
