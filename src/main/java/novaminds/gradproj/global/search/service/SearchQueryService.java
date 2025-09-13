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
        // 1. 검색어가 비어있으면 빈 결과 반환
        if (keyword == null || keyword.isBlank()) {
            return SearchConverter.toIngredientSearchListDTO(List.of());
        }

        // 2. 다중 필드 검색 쿼리 생성 (bool + should 조합)
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                // 2-1) Text 필드: 형태소 분석 + 퍼지 매칭 (오타 허용)
                                .should(s -> s
                                        .match(m -> m
                                                .field("ingredient_name")
                                                .query(keyword)
                                                .fuzziness("AUTO") // 오타 허용
                                                .boost(3.0f) // 가중치 3배
                                        )
                                )
                                // 2-2) 자동완성 필드: 부분 일치 검색
                                .should(s -> s
                                        .match(m -> m
                                                .field("ingredient_name.auto")
                                                .query(keyword)
                                                .boost(2.0f) // 가중치 2배
                                        )
                                )
                                // 2-3) 키워드 필드: 정확한 일치 (가장 높은 점수)
                                .should(s -> s
                                        .term(t -> t
                                                .field("ingredient_name.kw")
                                                .value(keyword)
                                                .boost(50.0f) // 가중치 50배
                                        )
                                )
                                .minimumShouldMatch("1") // 최소 1개 조건은 만족해야 함
                        )
                )
                .withPageable(PageRequest.of(page, size)) // 페이징 설정
                .build();

        // 3. 검색 실행 및 결과 추출
        SearchHits<IngredientDocument> hits = operations.search(query, IngredientDocument.class);
        List<IngredientDocument> docs = hits.getSearchHits().stream()
                .map(SearchHit::getContent).toList();

        // 4. DTO 변환 후 반환
        return SearchConverter.toIngredientSearchListDTO(docs);
    }
}