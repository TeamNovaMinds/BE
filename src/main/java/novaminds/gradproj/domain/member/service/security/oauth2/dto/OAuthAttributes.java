package novaminds.gradproj.domain.member.service.security.oauth2.dto;

import lombok.Builder;
import lombok.Getter;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.member.entity.SocialType;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String name;
    private String email;
    private String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String name, String email, String picture, String providerId) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.providerId = providerId;
    }

    // 소셜 로그인 타입에 따른 분기 처리
    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> ofGoogle(attributes);
            case "naver" -> ofNaver(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        };
    }

    // 구글 OAuth 처리
    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .providerId(String.valueOf(attributes.get("sub")))
                .attributes(attributes)
                .build();
    }

    // 네이버 OAuth 처리 (향후 확장용)
    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .providerId((String) response.get("id"))
                .attributes(attributes)
                .build();
    }

    // 카카오 OAuth 처리 (향후 확장용)
    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .providerId(String.valueOf(attributes.get("id")))
                .attributes(attributes)
                .build();
    }

    // User 엔티티 생성 메서드
    public Member toEntity(String loginId, SocialType socialType) {
        String tempNickname = "oauth_" + UUID.randomUUID().toString().substring(0, 8);

        return Member.builder()
                .loginId(loginId)
                .email(email)
                .name(name)
                .nickname(tempNickname)
                .password(UUID.randomUUID().toString()) // OAuth는 패스워드 불필요
                .providerId(providerId)
                .role(Role.USER)
                .socialType(socialType)
                .isProfileCompleted(false)
                .build();
    }
}