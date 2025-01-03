package unip.universityInParty.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.oauth.dto.social.OAuth2Response;

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

    private String profileImage;

    private boolean auth;

    private int point;

    @Enumerated(EnumType.STRING)
    private Status status;

    public List<String> getRoles() {
        return List.of(this.role.name());
    }

    public static Member defaultCreateMember(OAuth2Response oAuth2Response, String username){
        return Member.builder()
            .username(username)
            .name(oAuth2Response.getName())
            .email(oAuth2Response.getEmail())
            .profileImage("https://quddaztestbucket.s3.ap-northeast-2.amazonaws.com/default.png")
            .role(Role.GUEST)
            .point(0)
            .auth(false)
            .status(Status.BORED)
            .build();
    }
}