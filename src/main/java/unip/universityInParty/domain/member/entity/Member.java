package unip.universityInParty.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profile_image;

    private boolean auth;

    private int point;

    @Enumerated(EnumType.STRING)
    private Status status;


    public void plusPoint(int point){
        this.point += point;
    }
}