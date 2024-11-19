package unip.universityInParty.domain.oauth.utils.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.member.exception.MemberErrorCode;
import unip.universityInParty.domain.oauth.exception.OAuthErrorCode;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private Key key;
    private static final String MEMBER_ROLE = "role";

    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;


    @PostConstruct
    public void setKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Long memberId, List<String> roles) {
        long now = (new Date()).getTime();

        // Access token 유효 기간 설정
        Date accessValidity = new Date(now + jwtProperties.accessTokenExpiration());

        return Jwts.builder()
            .setIssuedAt(new Date(now))
            .setExpiration(accessValidity)
            .setIssuer(jwtProperties.issuer())
            .setSubject(memberId.toString())
            .addClaims(Map.of(MEMBER_ROLE, roles))
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(Long memberId, List<String> roles) {
        long now = (new Date()).getTime();

        // Refresh token 유효 기간 설정
        Date refreshValidity = new Date(now + jwtProperties.refreshTokenExpiration());

        // Refresh token 생성
        return Jwts.builder()
            .setIssuedAt(new Date(now))
            .setExpiration(refreshValidity)
            .setIssuer(jwtProperties.issuer())
            .setSubject(memberId.toString())
            .addClaims(Map.of(MEMBER_ROLE, roles))
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }


    public boolean validateToken(final String token) {
        try {
            log.info("now date: {}", new Date());
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (Exception e) {
            log.error("Token validation error: ", e);
            return false;
        }
    }

    public Member getMember(String token) {
        Long id = Long.parseLong(Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).getBody().getSubject());

        log.info("in getMember() socialId: {}", id);

        return memberRepository.findById(id)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}