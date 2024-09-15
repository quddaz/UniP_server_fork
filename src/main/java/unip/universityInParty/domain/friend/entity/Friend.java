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
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id1")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id2")
    private Member member2;
}
