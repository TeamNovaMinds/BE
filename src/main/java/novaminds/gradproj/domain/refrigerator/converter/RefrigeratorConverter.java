package novaminds.gradproj.domain.refrigerator.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkinImage;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefrigeratorConverter {

    public static RefrigeratorResponseDTO.RefrigeratorSkinResponse toRefrigeratorSkinResponse(
            RefrigeratorSkin refrigeratorSkin,
            boolean owned,
            boolean equipped
    ) {

        var skinImageUrls = refrigeratorSkin.getRefrigeratorSkinImages().stream()
                .map(RefrigeratorConverter::toRefrigeratorSkinImageUrlResponse)
                .toList();

        return RefrigeratorResponseDTO.RefrigeratorSkinResponse.builder()
                .id(refrigeratorSkin.getId())
                .name(refrigeratorSkin.getSkinName())
                .description(refrigeratorSkin.getDescription())
                .skinImageUrls(skinImageUrls)
                .owned(owned)
                .equipped(equipped)
                .build();
    }

    public static RefrigeratorResponseDTO.RefrigeratorSkinListResponse toRefrigeratorSkinListResponse(
            RefrigeratorSkin refrigeratorSkin,
            boolean owned,
            boolean equipped
    ) {
        // 첫 번째 이미지를 썸네일로 사용
        String thumbnailUrl = refrigeratorSkin.getRefrigeratorSkinImages().stream()
                .findFirst()
                .map(RefrigeratorSkinImage::getImageUrl)
                .orElse(null);

        return RefrigeratorResponseDTO.RefrigeratorSkinListResponse.builder()
                .id(refrigeratorSkin.getId())
                .name(refrigeratorSkin.getSkinName())
                .description(refrigeratorSkin.getDescription())
                .price(refrigeratorSkin.getPrice())
                .thumbnailUrl(thumbnailUrl)
                .owned(owned)
                .equipped(equipped)
                .build();
    }

    private static RefrigeratorResponseDTO.RefrigeratorSkinImageUrlResponse toRefrigeratorSkinImageUrlResponse(RefrigeratorSkinImage skinImage) {
        return RefrigeratorResponseDTO.RefrigeratorSkinImageUrlResponse.builder()
                .imageOrder(skinImage.getImageOrder())
                .imageUrl(skinImage.getImageUrl())
                .build();
    }

    public static MemberRefrigeratorSkin toMemberRefrigeratorSkin(Member member, RefrigeratorSkin skin) {
        return MemberRefrigeratorSkin.builder()
                .member(member)
                .skin(skin)
                .build();
    }

    public static RefrigeratorResponseDTO.IngredientResponse toIngredientResponse(List<StoredItem> storedItems) {
        var storedIngredientResponses = storedItems.stream()
                .map(RefrigeratorConverter::toStoredIngredientResponse)
                .toList();

        return RefrigeratorResponseDTO.IngredientResponse.builder()
                .addedCount(storedItems.size())
                .storedIngredients(storedIngredientResponses)
                .build();
    }

    public static RefrigeratorResponseDTO.StoredIngredientResponse toStoredIngredientResponse(StoredItem storedItem) {

        String storageType = switch (storedItem.getStorageType()) {
            case ROOM_TEMPERATURE -> "실온";
            case REFRIGERATOR -> "냉장";
            case FREEZER -> "냉동";
        };

        return RefrigeratorResponseDTO.StoredIngredientResponse.builder()
                .id(storedItem.getId())
                .ingredientName(storedItem.getIngredient().getIngredientName())
                .expirationDate(storedItem.getExpirationDate())
                .storageType(storageType)
                .build();
    }

    public static StoredItem toStoredItem(
            Refrigerator refrigerator, 
            Ingredient ingredient, 
            LocalDate expirationDate, 
            StorageType storageType
    ) {
        return StoredItem.builder()
                .refrigerator(refrigerator)
                .ingredient(ingredient)
                .expirationDate(expirationDate)
                .storageType(storageType)
                .build();
    }
}
