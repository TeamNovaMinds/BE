package novaminds.gradproj.global.search.web.controller;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.global.search.service.IngredientIndexingService;
import novaminds.gradproj.global.search.service.SearchQueryService;
import novaminds.gradproj.global.search.web.dto.SearchResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final IngredientIndexingService ingredientIndexingService;
    private final SearchQueryService searchQueryService;

    @PostMapping("/admin/reindex")
    public ApiResponse<String> reindexIngredients() {
        ingredientIndexingService.indexIngredients();
        return ApiResponse.onSuccess("인덱싱이 성공적으로 완료되었습니다.");
    }

    @GetMapping("/ingredients")
    public ApiResponse<SearchResponseDTO.IngredientSearchList> searchIngredients(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        SearchResponseDTO.IngredientSearchList searchResults = searchQueryService.searchIngredients(keyword, page, size);
        return ApiResponse.onSuccess(searchResults);
    }
}
