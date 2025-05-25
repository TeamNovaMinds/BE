package novaminds.gradproj.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.user.User;

public class AuthResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String loginId;
        private String email;
        private String nickname;
        private String name;
        private String role;

        public static LoginResponse from(User user) {
            return LoginResponse.builder()
                    .loginId(user.getLoginId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {
        private String loginId;
        private String email;
        private String name;
        private String nickname;

        public static SignupResponse from(User user) {
            return SignupResponse.builder()
                    .loginId(user.getLoginId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}