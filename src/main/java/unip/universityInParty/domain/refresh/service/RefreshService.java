package unip.universityInParty.domain.refresh.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.refresh.entity.Refresh;
import unip.universityInParty.domain.refresh.repository.RefreshRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.OAuthErrorCode;
import unip.universityInParty.global.security.jwt.JwtUtil;
import unip.universityInParty.global.util.CookieStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshService {
    private final RefreshRepository refreshRepository;
    private final long refreshTokenExpireTime = 86400L; // 1일
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CookieStore cookieStore;

    @Transactional
    public void addRefresh(String username, String token) {
        Refresh refresh = Refresh.builder()
            .username(username)
            .token(token)
            .expiration(refreshTokenExpireTime) // Redis의 TTL(Time-to-live) 기능 사용
            .build();
        refreshRepository.save(refresh);
    }


    public Boolean existsByUsername(String username) {
        return refreshRepository.existsByUsername(username);
    }


    @Transactional
    public void deleteByUsername(String username) {
        refreshRepository.deleteByUsername(username);
    }

    public List<Refresh> get(){return refreshRepository.findAll();}
    @Transactional
    public void refreshAccessToken(String refreshToken, HttpServletResponse response) {

        jwtUtil.validateToken(refreshToken);
        // DB에 저장되어 있는지 확인
        String username = jwtUtil.getUsername(refreshToken);
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 새로운 Access 및 Refresh 토큰 생성
        String newAccess = jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()), "access", member.isAuth());
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()), "refresh", member.isAuth());

        //Redis Refresh 토큰 갱신
        deleteByUsername(username);
        addRefresh(username, newRefresh);
        // Refresh 토큰 저장 및 응답 설정
        response.addCookie(cookieStore.createCookie(newRefresh));
        response.setHeader("access", newAccess);

    }
}