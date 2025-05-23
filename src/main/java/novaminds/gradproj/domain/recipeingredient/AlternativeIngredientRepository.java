package novaminds.gradproj.domain.recipeingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlternativeIngredientRepository extends JpaRepository<AlternativeIngredient, Long> {
    //특정 레시피 재료의 대체 재료 목록 조회
    List<AlternativeIngredient> findByRecipeIngredientId(Long recipeIngredientId);

    //특정 재료가 대체 재료로 사용되는 모든 경우 조회
    List<AlternativeIngredient> findByAlternativeIngredientId(Long ingredientId);
}