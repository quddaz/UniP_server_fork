package unip.universityInParty.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.global.exception.errorCode.MemberErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDTO getMemberByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        return MemberDTO.builder()
            .id(member.getId())
            .username(member.getUsername())
            .auth(member.isAuth())
            .role(String.valueOf(member.getRole()))
            .profile_image(member.getProfile_image())
            .name(member.getName())
            .status(member.getStatus())
            .build();
    }
}