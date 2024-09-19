package unip.universityInParty.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("access");

        // AccessToken이 없거나, 만료된 경우
        if (accessToken == null) {
            throw new CustomException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        if (jwtUtil.isExpired(accessToken)) {
            throw new CustomException(OAuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }

        // 카테고리가 Access인지 확인
        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            throw new CustomException(OAuthErrorCode.INVALID_ACCESS_TOKEN);
        }

        // 학교 인증을 했는지 확인
        if(!jwtUtil.getAuth(accessToken)){
            throw new CustomException(MemberErrorCode.NO_UNI_VERIFICATION_ACCESS);
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

