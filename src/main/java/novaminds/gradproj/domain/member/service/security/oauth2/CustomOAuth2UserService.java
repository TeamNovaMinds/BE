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

import java.util.Map;
import java.util.Optional;

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
        String providerId = oAuthAttributes.getProviderId();

        // loginId 생성
        String loginId = generateLoginId(registrationId.toUpperCase(), providerId);

        // 사용자 조회 또는 생성(업데이트)
        Member member = saveOrUpdateAndOnBoard(loginId, email, name, providerId, registrationId);

        return new PrincipalDetails(member, attributes);
    }


    private Member saveOrUpdateAndOnBoard(String loginId, String email, String name,
                                          String providerId, String registrationId) {
        // 기존 사용자 조회
        Optional<Member> memberOpt = memberRepository.findById(loginId);

        if (memberOpt.isPresent()) {
            // 기존 유저의 경우
            Member member = memberOpt.get();
            member.updateOAuthInfo(name);
            return memberRepository.save(member);
        } else {
            // 신규 유저의 경우
            OAuthAttributes oAuthAttributes = OAuthAttributes.builder()
                    .name(name)
                    .email(email)
                    .providerId(providerId)
                    .build();
            Member newMember = oAuthAttributes.toEntity(loginId, SocialType.valueOf(registrationId.toUpperCase()));

            // DB에 저장 먼저 한 후
            Member savedMember = memberRepository.save(newMember);

            // 냉장고 할당 및 생성
            memberOnboardingService.setupDefaultResources(savedMember);

            return savedMember;
        }
    }

    private String generateLoginId(String provider, String providerId) {
        return provider + "_" + providerId;
    }
}