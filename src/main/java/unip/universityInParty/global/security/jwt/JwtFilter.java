package unip.universityInParty.global.security.jwt;

import io.jsonwebtoken.JwtException;
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
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();


        try {
            String accessToken = getAccessToken(request);
            validateToken(accessToken);
            setAuthentication(accessToken);
            log.info("User authenticated: {}", jwtUtil.getUsername(accessToken));
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    private String getAccessToken(HttpServletRequest request) throws JwtException {
        String accessToken = request.getHeader("access");
        if (accessToken == null) {
            throw new JwtException("Access token is missing");
        }
        return accessToken;
    }

    private void validateToken(String accessToken) throws JwtException {
        if (jwtUtil.isExpired(accessToken)) {
            throw new JwtException("Access token has expired");
        }

        if (!"access".equals(jwtUtil.getCategory(accessToken))) {
            throw new JwtException("Invalid token category");
        }
    }

    private void setAuthentication(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);
        CustomUserDetails customUserDetails = userDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(
            customUserDetails, null, customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}


