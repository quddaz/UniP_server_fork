package unip.universityInParty.global.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.member.dto.MemberDTO;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.oauth2.dto.GoogleResponse;
import unip.universityInParty.global.oauth2.dto.NaverResponse;
import unip.universityInParty.global.oauth2.dto.OAuth2Response;
import unip.universityInParty.global.security.custom.CustomUserDetails;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        // OAuth2Response를 제공업체에 따라 추출
        OAuth2Response oAuth2Response = getOAuth2Response(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (oAuth2Response == null) {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 제공업체입니다.");
        }

        // 사용자 식별자 생성
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // 사용자 정보를 업데이트하거나 새로 생성
        Member member = updateOrCreateMember(username, oAuth2Response);

        // CustomOAuth2User를 생성하여 반환
        MemberDTO memberDTO = MemberDTO.builder()
            .role(String.valueOf(member.getRole()))
            .username(username)
            .name(member.getName()) // name 필드 추가
            .profile_image(member.getProfile_image())
            .build();

        return new CustomUserDetails(memberDTO);
    }

    private OAuth2Response getOAuth2Response(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverResponse(attributes);
            case "google" -> new GoogleResponse(attributes);
            default -> null;
        };
    }

    private Member updateOrCreateMember(String username, OAuth2Response oAuth2Response) {
        Member member = memberRepository.findByUsername(username)
            .orElse(null);

        if (member == null) {
            // 새 사용자 생성
            member = Member.builder()
                .username(username)
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .profile_image(oAuth2Response.getProfileImage())
                .role(Role.ROLE_USER)
                .point(0)
                .auth(false)
                .status(Status.BORED)
                .build();
        } else {
            member.setProfile_image(oAuth2Response.getProfileImage());
            // 기존 사용자 업데이트
            member.setEmail(oAuth2Response.getEmail());
            member.setName(oAuth2Response.getName());
        }

        // 사용자 정보를 데이터베이스에 저장
        return memberRepository.save(member);
    }
}