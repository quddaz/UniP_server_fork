package unip.universityInParty.domain.refresh.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.domain.refresh.entity.Refresh;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;
import unip.universityInParty.global.exception.errorCode.OAuthErrorCode;
import unip.universityInParty.global.security.jwt.JwtUtil;
import unip.universityInParty.global.util.CookieStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RefreshService {
    private final RedisTemplate<String, Refresh> redisTemplate;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final CookieStore cookieStore;

    // Refresh 객체를 Redis에 추가
    @Transactional
    public void addRefresh(String username, String token) {
        if (redisTemplate.hasKey(username)) {
            deleteByUsername(username);
        }
        Refresh refresh = Refresh.builder()
            .username(username)
            .token(token)
            .build();
        redisTemplate.opsForValue().set(username, refresh); // username을 키로 저장
    }

    // 특정 username에 대한 Refresh 객체 존재 여부 확인
    public Boolean existsByUsername(String username) {
        return redisTemplate.opsForValue().get(username) != null;
    }

    // username으로 Refresh 객체 삭제
    @Transactional
    public void deleteByUsername(String username) {
        redisTemplate.delete(username); // Redis에서 삭제
    }

    // 모든 Refresh 객체 조회
    public List<Refresh> get() {
        List<Refresh> refreshList = new ArrayList<>();

        // 모든 Refresh 키 조회 (username 기준)
        Set<String> keys = redisTemplate.keys("*"); // 모든 키를 가져옴
        if (keys != null) {
            for (String key : keys) {
                Refresh refresh = redisTemplate.opsForValue().get(key);
                if (refresh != null) {
                    refreshList.add(refresh);
                }
            }
        }
        return refreshList;
    }

    @Transactional
    public void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        // 토큰 유효성 검사
        jwtUtil.validateToken(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String username = jwtUtil.getUsername(refreshToken);
        Refresh refresh = redisTemplate.opsForValue().get(username);
        log.info("{}, {}",username,refresh);
        if (refresh == null || !refresh.getToken().equals(refreshToken)) {
            throw new CustomException(OAuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 새 토큰 생성 및 갱신
        String newAccess = jwtUtil.createAccessJwt(member.getUsername(), String.valueOf(member.getRole()));
        String newRefresh = jwtUtil.createRefreshJwt(member.getUsername(), String.valueOf(member.getRole()));

        addRefresh(username, newRefresh);

        response.addCookie(cookieStore.createCookie(newRefresh));
        response.setHeader("access", newAccess);
    }
}
