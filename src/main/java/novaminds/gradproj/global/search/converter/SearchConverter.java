package novaminds.gradproj.global.search.converter;

import novaminds.gradproj.global.search.IngredientDocument;
import novaminds.gradproj.global.search.web.dto.SearchResponseDTO;

import java.util.List;

public class SearchConverter {

    public static SearchResponseDTO.IngredientSearchResult toIngredientSearchResultDTO(IngredientDocument document) {
        return SearchResponseDTO.IngredientSearchResult.builder()
                .id(document.getId())
                .name(document.getIngredientName())
                .category(document.getCategoryName())
                .imageUrl(document.getImageUrl())
                .build();
    }

    public static SearchResponseDTO.IngredientSearchList toIngredientSearchListDTO(List<IngredientDocument> documents) {
        List<SearchResponseDTO.IngredientSearchResult> results = documents.stream()
                .map(SearchConverter::toIngredientSearchResultDTO)
                .toList();

        return SearchResponseDTO.IngredientSearchList.builder()
                .results(results)
                .build();
    }
}
