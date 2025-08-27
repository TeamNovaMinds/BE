package novaminds.gradproj.domain.refrigerator.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RefrigeratorSkinImageUrlResponse {
        private int imageOrder;
        private String ImageUrl;
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
    }
}
