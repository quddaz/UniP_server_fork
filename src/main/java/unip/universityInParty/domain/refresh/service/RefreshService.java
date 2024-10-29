package unip.universityInParty.domain.refresh.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CookieStore cookieStore;

    @Transactional
    public void addRefresh(String username, String token) {

        Refresh refresh = Refresh.builder()
            .username(username)
            .token(token)
            .build();
        refreshRepository.save(refresh);  // username을 키로 저장
    }


    public Boolean existsByUsername(String username) {
        return refreshRepository.existsByUsername(username);
    }


    @Transactional
    public void deleteByUsername(String username) {
        refreshRepository.deleteById(username);
    }

    public List<Refresh> get(){return (List<Refresh>) refreshRepository.findAll();}
    @Transactional
    public void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        // 토큰 유효성 검사
        jwtUtil.validateToken(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String username = jwtUtil.getUsername(refreshToken);
        if (!refreshRepository.existsByToken(refreshToken)) {
            throw new CustomException(OAuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 새 토큰 생성 및 갱신
        String newAccess = jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()));
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()));

        deleteByUsername(member.getUsername());
        addRefresh(username, newRefresh);

        response.addCookie(cookieStore.createCookie(newRefresh));
        response.setHeader("access", newAccess);
    }
}