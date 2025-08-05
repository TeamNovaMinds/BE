package novaminds.gradproj.domain.member.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.member.entity.MemberInterestCategory;

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

        public static LoginResponse from(Member member) {
            return LoginResponse.builder()
                    .loginId(member.getLoginId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .name(member.getName())
                    .profileImg(member.getProfileImage())
                    .role(member.getRole().name())
                    .isProfileCompleted(member.isProfileCompleted())
                    .interestCategories(member.getInterestCategories().stream()
                            .map(MemberInterestCategory::getCategory)
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
        private String name;
        private boolean isProfileCompleted;
        private List<RecipeCategory> interestCategories;

        public static SignupResponse from(Member member) {
            return SignupResponse.builder()
                    .loginId(member.getLoginId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .isProfileCompleted(member.isProfileCompleted())
                    .interestCategories(member.getInterestCategories().stream()
                            .map(MemberInterestCategory::getCategory)
                            .toList())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdditionalInfoResponse {
        private String name;
        private String email;
        private String loginId;
        private String nickname;
        private List<RecipeCategory> interestCategories;
        private String profileImgUrl;
        private boolean isProfileCompleted;

        public static AdditionalInfoResponse from(Member member) {
            return AdditionalInfoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .interestCategories(member.getInterestCategories().stream()
                    .map(MemberInterestCategory::getCategory)
                    .toList())
                .profileImgUrl(member.getProfileImage())
                .isProfileCompleted(member.isProfileCompleted())
                .build();
        }
    }
}