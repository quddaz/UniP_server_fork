package unip.universityInParty.domain.refresh.entity;



import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh")
public class Refresh {

    @Id
    private Long id;

    @Indexed
    private String username;

    @Indexed
    private String token;

    @TimeToLive
    private long expiration;

}