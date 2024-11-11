package unip.universityInParty.domain.oauth.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import unip.universityInParty.domain.oauth.dto.AuthMember;
import unip.universityInParty.domain.oauth.dto.social.GoogleResponse;
import unip.universityInParty.domain.oauth.dto.social.NaverResponse;
import unip.universityInParty.domain.oauth.dto.social.OAuth2Response;
import unip.universityInParty.domain.member.entity.Enum.Role;
import unip.universityInParty.domain.member.entity.Enum.Status;
import unip.universityInParty.domain.member.entity.Member;
import unip.universityInParty.domain.member.repository.MemberRepository;
import unip.universityInParty.global.exception.custom.CustomException;
import unip.universityInParty.domain.oauth.exception.OAuthErrorCode;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public AuthMember loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // OAuth2 사용자 정보를 가져옴

        // OAuth2Response를 제공업체에 따라 추출
        OAuth2Response oAuth2Response = getOAuth2Response(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        // 사용자 정보 로드 또는 생성
        Member member = getOrGenerateMember(username,oAuth2Response);

        // AuthUser 생성 및 반환
        return AuthMember.builder()
            .id(member.getId())
            .name(member.getName())
            .username(username)
            .auth(member.isAuth())
            .status(member.getStatus())
            .profile_image(member.getProfile_image())
            .roles(List.of(member.getRole().name()))
            .build();
    }

    private OAuth2Response getOAuth2Response(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "naver" -> new NaverResponse(attributes);
            case "google" -> new GoogleResponse(attributes);
            default -> throw new CustomException(OAuthErrorCode.LOGIN_TYPE_NOT_FOUND);
        };
    }

    private Member getOrGenerateMember(String username, OAuth2Response oAuth2Response) {
        return memberRepository.findByUsername(username)
            .orElseGet(() -> memberRepository.save(Member.builder()
                .username(username)
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .profile_image(oAuth2Response.getProfileImage())
                .role(Role.GUEST)
                .point(0)
                .auth(false)
                .status(Status.BORED)
                .build()));
    }
}
