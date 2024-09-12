package unip.universityInParty.global.security.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username);
        MemberDTO memberDTO = MemberDTO.builder()
            .username(member.getUsername())
            .name(member.getName())
            .build();
        return new CustomUserDetails(memberDTO);
    }
}
