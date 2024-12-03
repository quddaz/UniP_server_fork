package unip.universityInParty.domain.oauth.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.oauth.utils.ResponseUtil;
import unip.universityInParty.domain.oauth.utils.jwt.JwtTokenProvider;
import unip.universityInParty.domain.oauth.refresh.service.RefreshService;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshService refreshService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthMember authUser = (AuthMember) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.createAccessToken(authUser.getId(), authUser.getRoles());
        ResponseCookie refreshToken = jwtTokenProvider.createRefreshToken(authUser.getId(), authUser.getRoles());

        refreshService.addRefresh(authUser.getId(), refreshToken.getValue());

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie", refreshToken.toString());

    }
}