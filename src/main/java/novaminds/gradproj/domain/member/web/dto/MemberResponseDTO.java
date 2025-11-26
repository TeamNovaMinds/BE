package novaminds.gradproj.domain.member.web.dto;

import java.util.List;
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
        private String accessToken;  // 모바일용 토큰
        private String refreshToken; // 모바일용 토큰

        public static LoginResponse from(Member member) {
            return LoginResponse.builder()
                    .nickname(member.getNickname())
                    .name(member.getName())
                    .profileImg(member.getProfileImage())
                    .isProfileCompleted(member.isProfileCompleted())
                    .build();
        }

        public static LoginResponse from(Member member, String accessToken, String refreshToken) {
            return LoginResponse.builder()
                    .nickname(member.getNickname())
                    .name(member.getName())
                    .profileImg(member.getProfileImage())
                    .isProfileCompleted(member.isProfileCompleted())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
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
        private String accessToken;  // 모바일용 토큰
        private String refreshToken; // 모바일용 토큰

        public static SignupResponse from(Member member) {
            return SignupResponse.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .isProfileCompleted(member.isProfileCompleted())
                    .build();
        }

        public static SignupResponse from(Member member, String accessToken, String refreshToken) {
            return SignupResponse.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .isProfileCompleted(member.isProfileCompleted())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
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
        private String accessToken;  // 모바일용 토큰 (part2에서만 사용)
        private String refreshToken; // 모바일용 토큰 (part2에서만 사용)

        public static AdditionalInfoResponse from(Member member) {
            return AdditionalInfoResponse.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImage())
                .isProfileCompleted(member.isProfileCompleted())
                .build();
        }

        public static AdditionalInfoResponse from(Member member, String accessToken, String refreshToken) {
            return AdditionalInfoResponse.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImage())
                .isProfileCompleted(member.isProfileCompleted())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
        private boolean following;  // 현재 조회하는 사람이 이 작성자를 팔로우 중인지
        private boolean myself;     // 현재 조회하는 사람이 이 작성자 본인인지
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileImageResponse {
        private String profileImgUrl;

        public static UpdateProfileImageResponse from(Member member) {
            return UpdateProfileImageResponse.builder()
                .profileImgUrl(member.getProfileImage())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInfoResponse {
        private String nickname;
        private Integer followingCount;
        private Integer followerCount;
        private Integer point;
        private String profileImgUrl;

        public static MyInfoResponse from(Member member) {
            return MyInfoResponse.builder()
                .nickname(member.getNickname())
                .followingCount(member.getFollowings().size())
                .followerCount(member.getFollowers().size())
                .point(member.getPoint())
                .profileImgUrl(member.getProfileImage())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointRankingResponse {
        private Long rank;
        private String nickname;
        private String profileImgUrl;
        private Integer point;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Top8RankingResponse {
        private List<PointRankingResponse> rankings;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllRankingResponse {
        private List<PointRankingResponse> rankings;
        private String nextCursor;
        private Boolean hasNext;
    }
}