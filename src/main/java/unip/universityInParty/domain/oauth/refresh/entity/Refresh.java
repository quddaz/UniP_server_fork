package unip.universityInParty.domain.oauth.refresh.entity;


import org.springframework.data.annotation.Id; // Spring Data Redis의 @Id import

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


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
