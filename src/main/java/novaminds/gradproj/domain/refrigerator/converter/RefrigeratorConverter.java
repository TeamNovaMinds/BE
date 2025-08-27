package novaminds.gradproj.domain.refrigerator.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkinImage;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefrigeratorConverter {

    public static RefrigeratorResponseDTO.RefrigeratorSkinResponse toRefrigeratorSkinResponse(RefrigeratorSkin refrigeratorSkin, boolean owned) {

        var skinImageUrls = refrigeratorSkin.getRefrigeratorSkinImages().stream()
                .map(RefrigeratorConverter::toRefrigeratorSkinImageUrlResponse)
                .toList();

        return RefrigeratorResponseDTO.RefrigeratorSkinResponse.builder()
                .id(refrigeratorSkin.getId())
                .name(refrigeratorSkin.getSkinName())
                .description(refrigeratorSkin.getDescription())
                .skinImageUrls(skinImageUrls)
                .owned(owned)
                .build();
    }

    public static RefrigeratorResponseDTO.RefrigeratorSkinListResponse toRefrigeratorSkinListResponse(RefrigeratorSkin refrigeratorSkin, boolean owned) {
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
                .build();
    }

    private static RefrigeratorResponseDTO.RefrigeratorSkinImageUrlResponse toRefrigeratorSkinImageUrlResponse(RefrigeratorSkinImage skinImage) {
        return RefrigeratorResponseDTO.RefrigeratorSkinImageUrlResponse.builder()
                .imageOrder(skinImage.getImageOrder())
                .ImageUrl(skinImage.getImageUrl())
                .build();
    }
}
