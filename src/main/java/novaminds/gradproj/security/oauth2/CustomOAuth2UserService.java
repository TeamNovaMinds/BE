package novaminds.gradproj.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.user.Role;
import novaminds.gradproj.domain.user.SocialType;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.user.UserRepository;
import novaminds.gradproj.security.auth.PrincipalDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("🔄 [OAuth2 로그인] {} 로그인 시도", registrationId);

        // 사용자 정보 추출
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String providerId = String.valueOf(attributes.get("sub"));

        // loginId 생성
        String loginId = generateLoginId(registrationId.toUpperCase(), providerId);

        // 사용자 조회 또는 생성
        User user = userRepository.findById(loginId)
                .orElseGet(() -> createOAuth2User(loginId, email, name, providerId, registrationId));

        log.info("✅ [OAuth2 로그인] 성공 - loginId: {}, email: {}", user.getLoginId(), user.getEmail());

        return new PrincipalDetails(user, attributes);
    }

    private User createOAuth2User(String loginId, String email, String name, String providerId, String registrationId) {
        log.info("🆕 [OAuth2 회원가입] 새 사용자 생성 - loginId: {}", loginId);

        // 닉네임 생성 (이메일 @ 앞부분 사용)
        String nickname = email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 4);

        User newUser = User.builder()
                .loginId(loginId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .providerId(providerId)
                .role(Role.USER)
                .socialType(SocialType.valueOf(registrationId.toUpperCase()))
                .build();

        return userRepository.save(newUser);
    }

    private String generateLoginId(String provider, String providerId) {
        return provider + "_" + providerId;
    }
}