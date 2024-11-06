package unip.universityInParty.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;

    private String profile_image;

    private boolean auth;

    private int point;

    @Enumerated(EnumType.STRING)
    private Status status;


    public void plusPoint(int point){
        this.point += point;
    }
    public String getRoleKey() {
        return this.role.getKey();
    }
    public List<String> getRoles(){return List.of(this.role.name());}
}