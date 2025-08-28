package novaminds.gradproj.domain.member.service.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.SocialType;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import novaminds.gradproj.domain.member.service.security.oauth2.dto.OAuthAttributes;
import novaminds.gradproj.domain.member.service.MemberOnboardingService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberOnboardingService memberOnboardingService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, attributes);

        // 사용자 정보 추출
        String email = oAuthAttributes.getEmail();
        String name = oAuthAttributes.getName();
        String picture = oAuthAttributes.getPicture();
        String providerId = oAuthAttributes.getProviderId();

        // loginId 생성
        String loginId = generateLoginId(registrationId.toUpperCase(), providerId);

        // 사용자 조회 또는 생성(업데이트)
        Member member = saveOrUpdate(loginId, email, name, picture, providerId, registrationId);

        // 기본 냉장고 할당
        memberOnboardingService.setupDefaultResources(member);

        return new PrincipalDetails(member, attributes);
    }


    private Member saveOrUpdate(String loginId, String email, String name, String picture,
                              String providerId, String registrationId) {
        // 기존 사용자 조회
        Member member = memberRepository.findById(loginId)
                .map(entity -> entity.updateOAuthInfo(name, picture)) // 기존 사용자 정보 업데이트
                .orElseGet(() -> {
                    // 새 사용자 생성
                    OAuthAttributes oAuthAttributes = OAuthAttributes.builder()
                            .name(name)
                            .email(email)
                            .picture(picture)
                            .providerId(providerId)
                            .build();
                    return oAuthAttributes.toEntity(loginId, SocialType.valueOf(registrationId.toUpperCase()));
                });

        return memberRepository.save(member);
    }

    private String generateLoginId(String provider, String providerId) {
        return provider + "_" + providerId;
    }
}