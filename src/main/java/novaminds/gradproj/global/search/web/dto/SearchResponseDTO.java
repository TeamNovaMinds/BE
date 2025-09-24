package novaminds.gradproj.global.search.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class SearchResponseDTO {

    @Getter
    @Builder
    public static class IngredientSearchList {
        private List<IngredientSearchResult> results;
    }

    @Getter
    @Builder
    public static class IngredientSearchResult {
        private Long id;
        private String name;
        private String category;
        private String imageUrl;
    }
}
