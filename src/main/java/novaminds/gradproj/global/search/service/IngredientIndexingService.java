package novaminds.gradproj.global.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.global.search.IngredientDocument;
import novaminds.gradproj.global.search.repository.IngredientSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngredientIndexingService {

    private final IngredientRepository ingredientRepository; // DB에서 데이터를 읽어올 JpaRepository
    private final IngredientSearchRepository ingredientSearchRepository; // Elasticsearch에 데이터를 저장할 Repository

    @Transactional(readOnly = true)
    public void indexIngredients() {
        log.info(">>>> Ingredient 데이터 인덱싱 시작...");

        // 1. DB에서 모든 재료 데이터를 조회
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        if (allIngredients.isEmpty()) {
            log.info(">>>> 인덱싱할 재료 데이터가 없습니다.");
            return;
        }

        // 2. Ingredient 엔티티를 IngredientDocument로 변환
        List<IngredientDocument> ingredientDocuments = allIngredients.stream()
                .map(ingredient -> IngredientDocument.builder()
                        .id(ingredient.getId())
                        .ingredientName(ingredient.getIngredientName())
                        .ingredientNameKw(ingredient.getIngredientName())
                        .categoryName(ingredient.getIngredientCategory().getIngredientCategoryName())
                        .imageUrl(ingredient.getImageUrl())
                        .build())
                .toList();

        // 3. Elasticsearch에 변환된 데이터를 저장(인덱싱)
        ingredientSearchRepository.saveAll(ingredientDocuments);

        log.info(">>>> Ingredient 데이터 인덱싱 완료 ({}개)", ingredientDocuments.size());
    }
}
