package unip.universityInParty.domain.oauth.refresh.entity;


import org.springframework.data.annotation.Id; // Spring Data Redis의 @Id import

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 86400)
public class Refresh {

    @Id
    private Long id;

    private String token;

}
