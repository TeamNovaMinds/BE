package novaminds.gradproj.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.user.UserInterestCategory;

import java.util.List;

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
        private String profileImg;
        private String role;
        private boolean isProfileCompleted;
        private List<RecipeCategory> interestCategories;

        public static LoginResponse from(User user) {
            return LoginResponse.builder()
                    .loginId(user.getLoginId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .name(user.getName())
                    .profileImg(user.getProfileImage())
                    .role(user.getRole().name())
                    .isProfileCompleted(user.isProfileCompleted())
                    .interestCategories(user.getInterestCategories().stream()
                            .map(UserInterestCategory::getCategory)
                            .toList())
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
        private String nickname;
        private boolean isProfileCompleted;
        private List<RecipeCategory> interestCategories;

        public static SignupResponse from(User user) {
            return SignupResponse.builder()
                    .loginId(user.getLoginId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .isProfileCompleted(user.isProfileCompleted())
                    .interestCategories(user.getInterestCategories().stream()
                            .map(UserInterestCategory::getCategory)
                            .toList())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdditionalInfoResponse {
        private String loginId;
        private String nickname;
        private List<RecipeCategory> interestCategories;
        private String profileImgUrl;
        private boolean isProfileCompleted;

        public static AdditionalInfoResponse from(User user) {
            return AdditionalInfoResponse.builder()
                    .loginId(user.getLoginId())
                    .nickname(user.getNickname())
                    .interestCategories(user.getInterestCategories().stream()
                            .map(UserInterestCategory::getCategory)
                            .toList())
                    .profileImgUrl(user.getProfileImage())
                    .isProfileCompleted(user.isProfileCompleted())
                    .build();
        }
    }
}