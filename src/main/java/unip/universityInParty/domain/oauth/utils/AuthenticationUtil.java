package unip.universityInParty.domain.oauth.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AuthenticationUtil {

    public static void makeAuthentication(Member member) {
        // Authentication 정보 만들기
        AuthMember authUser = AuthMember.createMember(member);

        // ContextHolder 에 Authentication 정보 저장
        Authentication auth = AuthenticationUtil.getAuthentication(authUser);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private static Authentication getAuthentication(AuthMember authUser) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authUser.getAuthorities());

        return new UsernamePasswordAuthenticationToken(authUser, "", grantedAuthorities);
    }
}