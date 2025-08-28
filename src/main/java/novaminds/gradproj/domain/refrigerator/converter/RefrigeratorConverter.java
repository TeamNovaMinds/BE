package novaminds.gradproj.domain.refrigerator.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkinImage;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;

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
            Boolean equipped
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
                .ImageUrl(skinImage.getImageUrl())
                .build();
    }

    public static MemberRefrigeratorSkin toMemberRefrigeratorSkin(Member member, RefrigeratorSkin skin) {
        return MemberRefrigeratorSkin.builder()
                .member(member)
                .skin(skin)
                .build();
    }
}
