package unip.universityInParty.global.security.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import unip.universityInParty.domain.member.dto.MemberDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {
    private final MemberDTO memberDTO;

    @Override
    public String getPassword() {
        return null; // OAuth2 사용자에게는 비밀번호가 없으므로 null 반환
    }

    @Override
    public String getUsername() {
        return memberDTO.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(memberDTO.getRole()));
    }
    @Override
    public String getName() {
        return memberDTO.getName();
    }
    public boolean getAuth(){return  memberDTO.isAuth();}

    public Long getId(){return memberDTO.getId();}
}
