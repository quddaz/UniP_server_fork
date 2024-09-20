package unip.universityInParty.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.alarm.entity.Enum.AlarmCategory;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alarm {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "receiver_id", nullable = false)
    private Long receiver;
    @Column(name = "sender_id", nullable = false)
    private Long sender;

    @Enumerated(EnumType.STRING)
    private AlarmCategory alarmCategory;

    private LocalDateTime time;
}
