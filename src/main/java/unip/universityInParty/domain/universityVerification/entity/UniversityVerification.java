package unip.universityInParty.domain.universityVerification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import unip.universityInParty.domain.member.entity.Member;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "university_verification")
public class UniversityVerification {
    @Id
    private Long id;

    private String email;

    private String authCode;

    @TimeToLive
    private long expiration;
}
