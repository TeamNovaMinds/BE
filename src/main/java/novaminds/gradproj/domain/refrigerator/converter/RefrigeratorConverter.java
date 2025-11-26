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
import novaminds.gradproj.domain.refrigerator.repository.projection.StorageTypeCount;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public static RefrigeratorResponseDTO.MemberRefrigeratorResponse toMemberRefrigeratorResponse(
            Member targetMember,
            List<StoredItem> storedItems,
            boolean following,
            boolean myself,
            Long equippedSkinId
    ) {
        var storedIngredientResponses = storedItems.stream()
                .map(RefrigeratorConverter::toStoredIngredientResponse)
                .toList();

        return RefrigeratorResponseDTO.MemberRefrigeratorResponse.builder()
                .nickname(targetMember.getNickname())
                .profileImage(targetMember.getProfileImage())
                .following(following)
                .myself(myself)
                .equippedSkinId(equippedSkinId)
                .addedCount(storedItems.size())
                .storedIngredients(storedIngredientResponses)
                .build();
    }

    public static RefrigeratorResponseDTO.StoredIngredientResponse toStoredIngredientResponse(StoredItem storedItem) {

        String dDay = calculateDDay(storedItem.getExpirationDate());

        return RefrigeratorResponseDTO.StoredIngredientResponse.builder()
                .id(storedItem.getId())
                .ingredientId(storedItem.getIngredient().getId())
                .ingredientName(storedItem.getIngredient().getIngredientName())
                .imageUrl(storedItem.getIngredient().getImageUrl())
                .quantity(storedItem.getQuantity())
                .expirationDate(storedItem.getExpirationDate())
                .dDay(dDay)
                .storageType(storedItem.getStorageType())
                .version(storedItem.getVersion())
                .build();
    }

    public static StoredItem toStoredItem(
            Refrigerator refrigerator, 
            Ingredient ingredient,
            Integer quantity,
            LocalDate expirationDate,
            StorageType storageType
    ) {
        return StoredItem.builder()
                .refrigerator(refrigerator)
                .ingredient(ingredient)
                .quantity(quantity)
                .expirationDate(expirationDate)
                .storageType(storageType)
                .build();
    }

    public static RefrigeratorResponseDTO.StoredIngredientCount toStoredIngredientCount(StorageTypeCount storageTypeCount) {
        return RefrigeratorResponseDTO.StoredIngredientCount.builder()
                .refrigeratorCount(storageTypeCount.getRefrigeratorCount().intValue())
                .freezerCount(storageTypeCount.getFreezerCount().intValue())
                .roomTempCount(storageTypeCount.getRoomTempCount().intValue())
                .build();
    }

    public static RefrigeratorResponseDTO.MemberRefrigeratorSummary toMemberRefrigeratorSummary(
            Member targetMember,
            boolean following,
            Long equippedSkinId,
            long recipeCount,
            long followerCount,
            long followingCount,
            long pointRank
    ) {
        return RefrigeratorResponseDTO.MemberRefrigeratorSummary.builder()
                .nickname(targetMember.getNickname())
                .profileImage(targetMember.getProfileImage())
                .following(following)
                .equippedSkinId(equippedSkinId)
                .recipeCount(recipeCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .pointRank(pointRank)
                .build();
    }

    public static RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse toRefrigeratorSkinsPageResponse(
            List<RefrigeratorResponseDTO.RefrigeratorSkinListResponse> skins,
            Long nextCursor,
            boolean hasNext
    ) {
        return RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse.builder()
                .skins(skins)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    private static String calculateDDay(LocalDate expirationDate) {
        if (expirationDate == null) {
            return null;
        }

        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);

        if (daysUntil > 0) {
            return "D-" + daysUntil;
        } else if (daysUntil == 0) {
            return "D-Day";
        } else {
            return "D+" + Math.abs(daysUntil);
        }
    }

}
