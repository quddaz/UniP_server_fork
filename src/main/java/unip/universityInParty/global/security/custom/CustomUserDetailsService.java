package unip.universityInParty.global.security.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberDTO memberDTO = MemberDTO.builder()
            .id(member.getId())
            .username(member.getUsername())
            .auth(member.isAuth())
            .role(String.valueOf(member.getRole()))
            .profile_image(member.getProfile_image())
            .name(member.getName())
            .status(member.getStatus())
            .build();
        return new CustomUserDetails(memberDTO);
    }
}
