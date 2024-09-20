package unip.universityInParty.domain.party.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Member;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {
    @Id
    @GeneratedValue
    private long id;

    private String title;

    private String content;

    private int partyLimit;

    private int peopleCount;

    private LocalDateTime startTime; // LocalDateTime 사용

    private LocalDateTime endTime; // 변수명 소문자로 수정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public boolean isPartyFull() {
        return peopleCount >= partyLimit;
    }

    public void joinParty() {
        if (!isPartyFull()) {
            peopleCount++;
        } else {
            throw new IllegalStateException("Party is full.");
        }
    }

    public void leaveParty() {
        if (peopleCount > 0) {
            peopleCount--;
        } else {
            throw new IllegalStateException("No one to leave the party.");
        }
    }


}
