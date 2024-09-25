package unip.universityInParty.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.OAuthErrorCode;
import unip.universityInParty.global.security.custom.CustomUserDetails;
import unip.universityInParty.global.security.custom.CustomUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 URL을 가져와서 로깅
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");

        // AccessToken이 없거나, 만료된 경우
        if (accessToken == null) {
            log.warn("Access token is missing");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // 만료된 토큰 확인
        if (jwtUtil.isExpired(accessToken)) {
            log.warn("Access token has expired");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // 카테고리가 Access인지 확인
        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            log.info("category={}", jwtUtil.getCategory(accessToken));
            log.warn("Invalid token category");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String username = jwtUtil.getUsername(accessToken);


        // CustomOAuth2User 객체를 생성하여 사용자 정보를 설정
        CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
            customUserDetails,
            null,
            customUserDetails.getAuthorities()
        );


        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("User authenticated: {}", customUserDetails.getUsername());

        filterChain.doFilter(request, response);
    }
}

