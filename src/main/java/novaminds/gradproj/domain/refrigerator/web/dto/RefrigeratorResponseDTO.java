package novaminds.gradproj.domain.refrigerator.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

        @Schema(description = "재료 개수")
        private Integer quantity;

        @Schema(description = "보관 방식")
        private String storageType;

        @Schema(description = "유통기한")
        private LocalDate expirationDate;

        @Schema(description = "유통기한 D-Day")
        private String dDay;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoredIngredientCount {

        @Schema(description = "냉장 보관 개수")
        private int refrigeratorCount;

        @Schema(description = "냉동 보관 개수")
        private int freezerCount;

        @Schema(description = "실온 보관 개수")
        private int roomTempCount;

    }
}
