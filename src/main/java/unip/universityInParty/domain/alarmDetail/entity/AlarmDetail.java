package unip.universityInParty.domain.alarmDetail.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.alarm.entity.Alarm;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.party.entity.Party;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmDetail {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    @JoinColumn(name = "party_id")
    private Long party;
}
