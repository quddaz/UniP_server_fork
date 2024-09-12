package unip.universityInParty.domain.refresh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Refresh {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String refresh;

    private String expiration;
}