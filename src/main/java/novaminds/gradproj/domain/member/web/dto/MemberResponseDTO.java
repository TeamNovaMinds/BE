package novaminds.gradproj.domain.member.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;

public class MemberResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String nickname;
        private String name;
        private String profileImg;
        private boolean isProfileCompleted;

        public static LoginResponse from(Member member) {
            return LoginResponse.builder()
                    .nickname(member.getNickname())
                    .name(member.getName())
                    .profileImg(member.getProfileImage())
                    .isProfileCompleted(member.isProfileCompleted())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {
        private String email;
        private String name;
        private boolean isProfileCompleted;

        public static SignupResponse from(Member member) {
            return SignupResponse.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .isProfileCompleted(member.isProfileCompleted())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdditionalInfoResponse {
        private String name;
        private String nickname;
        private String profileImgUrl;
        private boolean isProfileCompleted;

        public static AdditionalInfoResponse from(Member member) {
            return AdditionalInfoResponse.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImage())
                .isProfileCompleted(member.isProfileCompleted())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorInfo {
        private String nickname;
        private String profileImageUrl;
    }
}