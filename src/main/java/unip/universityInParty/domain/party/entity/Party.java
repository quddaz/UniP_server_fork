package unip.universityInParty.domain.party.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.global.exception.custom.CustomException;


import java.time.LocalDateTime;
import unip.universityInParty.global.exception.errorCode.PartyErrorCode;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    private int partyLimit;

    private int peopleCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Version  // 낙관적 락을 위한 버전 필드 추가
    private int version;

    private boolean isClosed;

    public boolean isPartyFull() {
        return peopleCount >= partyLimit;
    }

    public void joinParty() {
        if (isPartyFull()) {
            throw new CustomException(PartyErrorCode.PARTY_FULL);
        }
        peopleCount++;
    }

    public void leaveParty() {
        if (peopleCount <= 0) {
            throw new CustomException(PartyErrorCode.NO_MEMBER_TO_LEAVE);
        }
        peopleCount--;
    }


}
