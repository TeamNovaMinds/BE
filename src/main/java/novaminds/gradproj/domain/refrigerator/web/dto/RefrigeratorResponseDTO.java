package novaminds.gradproj.domain.refrigerator.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;

import java.time.LocalDate;
import java.util.List;

public class RefrigeratorResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RefrigeratorSkinResponse {
        private Long id;
        private String name;
        private String description;
        private List<RefrigeratorSkinImageUrlResponse> skinImageUrls;
        private boolean owned;
        private boolean equipped;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RefrigeratorSkinImageUrlResponse {
        private int imageOrder;
        private String imageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RefrigeratorSkinsPageResponse {
        private List<RefrigeratorSkinListResponse> skins;
        private Long nextCursor;
        private boolean hasNext;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RefrigeratorSkinListResponse {
        private Long id;
        private String name;
        private String description;
        private Integer price;
        private String thumbnailUrl;
        private boolean owned;
        private boolean equipped;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IngredientResponse {

        private Long refrigeratorId;

        @Schema(description = "보관 중인 재료 개수")
        private int addedCount;
        
        @Schema(description = "재료 정보 리스트")
        private List<StoredIngredientResponse> storedIngredients;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoredIngredientResponse {

        @Schema(description = "보관된 재료 ID")
        private Long id;

        @Schema(description = "재료 ID")
        private Long ingredientId;

        @Schema(description = "재료명")
        private String ingredientName;

        @Schema(description = "재료 이미지 URL")
        private String imageUrl;

        @Schema(description = "재료 개수")
        private Integer quantity;

        @Schema(description = "보관 방식")
        private StorageType storageType;

        @Schema(description = "유통기한")
        private LocalDate expirationDate;

        @Schema(description = "유통기한 D-Day")
        private String dDay;

        @Schema(description = "버전 (동시성 제어용)")
        private Long version;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoredIngredientCount {

        private Long refrigeratorId;

        @Schema(description = "냉장 보관 개수")
        private int refrigeratorCount;

        @Schema(description = "냉동 보관 개수")
        private int freezerCount;

        @Schema(description = "실온 보관 개수")
        private int roomTempCount;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberRefrigeratorResponse {
        @Schema(description = "회원 닉네임")
        private String nickname;

        @Schema(description = "회원 프로필 이미지 URL")
        private String profileImage;

        @Schema(description = "현재 조회하는 사람이 이 회원을 팔로우 중인지")
        private boolean following;

        @Schema(description = "현재 조회하는 사람이 이 회원 본인인지")
        private boolean myself;

        @Schema(description = "회원이 장착 중인 냉장고 스킨 ID (장착 안 함 시 null)")
        private Long equippedSkinId;

        @Schema(description = "보관 중인 재료 개수")
        private int addedCount;

        @Schema(description = "재료 정보 리스트")
        private List<StoredIngredientResponse> storedIngredients;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberRefrigeratorSummary {
        @Schema(description = "회원 닉네임")
        private String nickname;

        @Schema(description = "회원 프로필 이미지 URL")
        private String profileImage;

        @Schema(description = "현재 조회하는 사람이 이 회원을 팔로우 중인지")
        private boolean following;

        @Schema(description = "회원이 장착 중인 냉장고 스킨 ID (장착 안 함 시 null)")
        private Long equippedSkinId;

        @Schema(description = "작성한 레시피 개수")
        private long recipeCount;

        @Schema(description = "팔로워 수")
        private long followerCount;

        @Schema(description = "팔로잉 수")
        private long followingCount;

        @Schema(description = "포인트 등수")
        private long pointRank;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "냉장고 초대 응답")
    public static class InvitationResponse {

        @Schema(description = "초대 ID")
        private Long id;

        @Schema(description = "초대한 사람 닉네임")
        private String inviterNickname;

        @Schema(description = "초대한 사람 프로필 이미지")
        private String inviterProfileImage;

        @Schema(description = "초대받은 사람 닉네임")
        private String inviteeNickname;

        @Schema(description = "초대받은 사람 프로필 이미지")
        private String inviteeProfileImage;

        @Schema(description = "초대 상태", allowableValues = {"PENDING", "ACCEPTED", "REJECTED", "CANCELED"})
        private String status;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "냉장고 초대 목록 응답")
    public static class InvitationListResponse {

        @Schema(description = "초대 목록")
        private List<InvitationResponse> invitations;
    }
}
