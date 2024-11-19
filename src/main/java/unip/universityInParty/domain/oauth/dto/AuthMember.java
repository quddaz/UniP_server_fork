package unip.universityInParty.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Builder
@Getter
@AllArgsConstructor
public class AuthMember implements OAuth2User {
    private final Long id;
    private final String name;
    private final String username;
    private final List<String> roles;
    private final Status status;
    private final boolean auth;
    private final String profileImage;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    public static AuthMember createMember(Member member, String username) {
        return AuthMember.builder()
            .id(member.getId())
            .name(member.getName())
            .username(username)
            .auth(member.isAuth())
            .status(member.getStatus())
            .profileImage(member.getProfileImage())
            .roles(List.of(member.getRole().name()))
            .build();
    }
    public static AuthMember createMember(Member member) {
        return AuthMember.builder()
            .id(member.getId())
            .name(member.getName())
            .auth(member.isAuth())
            .status(member.getStatus())
            .profileImage(member.getProfileImage())
            .roles(List.of(member.getRole().name()))
            .build();
    }

}
