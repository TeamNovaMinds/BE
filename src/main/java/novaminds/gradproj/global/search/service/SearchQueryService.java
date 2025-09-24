package novaminds.gradproj.global.search.service;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.global.search.IngredientDocument;
import novaminds.gradproj.global.search.converter.SearchConverter;
import novaminds.gradproj.global.search.web.dto.SearchResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchQueryService {

    private final ElasticsearchOperations operations;

    public SearchResponseDTO.IngredientSearchList searchIngredients(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            return SearchConverter.toIngredientSearchListDTO(List.of());
        }

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .query(keyword)
                                .fields("ingredientName", "ingredientNameKw")
                                .fuzziness("AUTO")
                        )
                )
                .withMinScore(2.0f)
                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<IngredientDocument> hits = operations.search(query, IngredientDocument.class);
        List<IngredientDocument> docs = hits.getSearchHits().stream()
                .map(SearchHit::getContent).toList();

        return SearchConverter.toIngredientSearchListDTO(docs);
    }
}