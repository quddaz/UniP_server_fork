package unip.universityInParty.domain.universityVerification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import unip.universityInParty.domain.member.entity.Member;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "universityVerification")
public class UniversityVerification {
    @Id
    private Long id;

    @Indexed
    private String email;

    private String authCode;

    @TimeToLive
    private long expiration;
}
