package unip.universityInParty.domain.oauth.utils.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secretKey,
    String issuer,
    Long accessTokenExpiration,
    Long refreshTokenExpiration) {

}