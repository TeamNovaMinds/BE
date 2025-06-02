package novaminds.gradproj.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.user.SocialType;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.user.UserRepository;
import novaminds.gradproj.security.auth.PrincipalDetails;
import novaminds.gradproj.security.oauth2.dto.OAuthAttributes;
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

    private final UserRepository userRepository;

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
        User user = saveOrUpdate(loginId, email, name, picture, providerId, registrationId);

        log.info("✅ [OAuth2 로그인] 성공 - loginId: {}, email: {}, 프로필 완료: {}",
                user.getLoginId(), user.getEmail(), user.isProfileCompleted());

        return new PrincipalDetails(user, attributes);
    }

    private User saveOrUpdate(String loginId, String email, String name, String picture,
                              String providerId, String registrationId) {

        // 기존 사용자 조회
        User user = userRepository.findById(loginId)
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

        return userRepository.save(user);
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