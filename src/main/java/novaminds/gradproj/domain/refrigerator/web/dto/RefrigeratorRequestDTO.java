package novaminds.gradproj.domain.refrigerator.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;

import java.util.List;

public class RefrigeratorRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class RefrigeratorSkinCreateRequest {

        @NotBlank(message = "스킨 이름은 필수 입력값입니다")
        @Size(max = 50, message = "스킨 이름은 50자 이하여야 합니다")
        private String skinName;

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다")
        private String description;

        @NotNull(message = "가격은 필수 입력값입니다")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다")
        private Integer price;

        @NotEmpty(message = "이미지는 최소 1개 이상 등록해야 합니다")
        private List<@NotBlank(message = "이미지 URL은 빈 값일 수 없습니다") String> imageUrls;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "냉장고에 재료 추가 요청")
    public static class AddIngredientRequest {

        @Schema(description = "재료 정보 리스트")
        @NotEmpty(message = "재료 정보는 최소 1개 이상이어야 합니다.")
        @Valid
        private List<IngredientItem> ingredients;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "개별 재료 정보")
    public static class IngredientItem {

        @Schema(description = "재료 ID")
        @NotNull(message = "재료 ID는 필수입니다.")
        private Long ingredientId;

        @Schema(description = "보관 방식", example = "REFRIGERATOR", 
                allowableValues = {"ROOM_TEMPERATURE", "REFRIGERATOR", "FREEZER"})
        @NotNull(message = "보관 방식은 필수입니다.")
        private StorageType storageType;
    }
}
