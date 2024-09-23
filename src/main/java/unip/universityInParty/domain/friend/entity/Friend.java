package unip.universityInParty.domain.friend.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Member;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromMember_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toMember_id")
    private Member toMember;
}
