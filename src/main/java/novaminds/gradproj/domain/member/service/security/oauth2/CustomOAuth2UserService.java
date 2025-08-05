package novaminds.gradproj.domain.member.service.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.handler.RefrigeratorSkinHandler;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.member.entity.SocialType;
import novaminds.gradproj.domain.member.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.member.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import novaminds.gradproj.domain.member.service.security.oauth2.dto.OAuthAttributes;
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
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        log.info("🔄 [OAuth2 로그인] {} 로그인 시도", registrationId);

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        // 사용자 정보 추출
        String email = oAuthAttributes.getEmail();
        String name = oAuthAttributes.getName();
        String picture = oAuthAttributes.getPicture();
        String providerId = oAuthAttributes.getProviderId();

        // loginId 생성
        String loginId = generateLoginId(registrationId.toUpperCase(), providerId);

        // 사용자 조회 또는 생성(업데이트)
        Member member = saveOrUpdate(loginId, email, name, picture, providerId, registrationId);

        createRefrigeratorForUser(member);

        log.info("✅ [OAuth2 로그인] 성공 - loginId: {}, email: {}, 프로필 완료: {}",
                member.getLoginId(), member.getEmail(), member.isProfileCompleted());

        return new PrincipalDetails(member, attributes);
    }

    private void createRefrigeratorForUser(Member member) {
        if (refrigeratorRepository.existsByMember(member)) {
            log.info("ℹ️ 기존 사용자 {}의 냉장고가 이미 존재하므로 생성을 건너뜁니다.", member.getLoginId());
            return;
        }

        // 냉장고 생성
        Refrigerator refrigerator = Refrigerator.builder()
                .member(member)
                .build();

        refrigeratorRepository.save(refrigerator);
        member.setRefrigerator(refrigerator);

        // 기본 스킨 찾기
        RefrigeratorSkin defaultSkin = refrigeratorSkinRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new RefrigeratorSkinHandler(ErrorStatus.DEFAULT_REFRIGERATOR_SKIN_NOT_FOUND));

        // 기본 스킨을 유저에게 부여하고 장착
        MemberRefrigeratorSkin userSkin = MemberRefrigeratorSkin.builder()
                .member(member)
                .skin(defaultSkin)
                .isEquipped(true)
                .build();

        memberRefrigeratorSkinRepository.save(userSkin);

        log.info("✅ [회원가입] 냉장고 및 기본 스킨 생성 완료 - userId: {}", member.getLoginId());
    }

    private Member saveOrUpdate(String loginId, String email, String name, String picture,
                              String providerId, String registrationId) {

        // 기존 사용자 조회
        Member member = memberRepository.findById(loginId)
                .map(entity -> entity.updateOAuthInfo(name, picture)) // 기존 사용자 정보 업데이트
                .orElseGet(() -> {
                    // 새 사용자 생성
                    log.info("🆕 [OAuth2 회원가입] 새 사용자 생성 - loginId: {}", loginId);

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

    public Map<String, Object> getAdditionalInfoRequirements() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "추가 정보 입력이 필요합니다");
        response.put("requiredFields", Arrays.asList(
                "profileImage", "nickname", "RecipeCategory"
        ));

        return response;
    }
}