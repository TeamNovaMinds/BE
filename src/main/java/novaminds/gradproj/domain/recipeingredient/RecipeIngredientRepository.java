package novaminds.gradproj.domain.recipeingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    //특정 레시피의 재료 목록 조회
    List<RecipeIngredient> findByRecipeIdOrderByIngredientOrder(Long recipeId);

    //대체 재료가 있는 레시피 재료들만 조회
    @Query("SELECT ri FROM RecipeIngredient ri LEFT JOIN FETCH ri.alternatives WHERE ri.recipe.id = :recipeId AND SIZE(ri.alternatives) > 0")
    List<RecipeIngredient> findByRecipeIdWithAlternatives(@Param("recipeId") Long recipeId);

    //특정 재료를 사용하는 모든 레시피 찾기
    List<RecipeIngredient> findByIngredientId(Long ingredientId);

    //특정 재료를 대체 재료로 사용하는 모든 레시피 찾기
    @Query("SELECT ri FROM RecipeIngredient ri JOIN ri.alternatives a WHERE a.alternativeIngredient.id = :ingredientId")
    List<RecipeIngredient> findByAlternativeIngredientId(@Param("ingredientId") Long ingredientId);

    //특정 레시피의 필수 재료만 조회
    List<RecipeIngredient> findByRecipeIdAndIsRequiredTrue(Long recipeId);

    //레시피 ID와 재료 ID로 특정 RecipeIngredient 조회
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId AND ri.ingredient.id = :ingredientId")
    RecipeIngredient findByRecipeIdAndIngredientId(@Param("recipeId") Long recipeId, @Param("ingredientId") Long ingredientId);

    //여러 재료 중 하나라도 포함하는 레시피 찾기 (OR 조건)
    @Query("SELECT DISTINCT ri.recipe.id FROM RecipeIngredient ri WHERE ri.ingredient.id IN :ingredientIds")
    List<Long> findRecipeIdsByIngredientIds(@Param("ingredientIds") List<Long> ingredientIds);

    //특정 재료들을 모두 포함하는 레시피 찾기 (AND 조건)
    @Query("SELECT ri.recipe.id FROM RecipeIngredient ri WHERE ri.ingredient.id IN :ingredientIds GROUP BY ri.recipe.id HAVING COUNT(DISTINCT ri.ingredient.id) = :ingredientCount")
    List<Long> findRecipeIdsContainingAllIngredients(@Param("ingredientIds") List<Long> ingredientIds, @Param("ingredientCount") long ingredientCount);

    //레시피에 필요한 재료 개수 카운트
    @Query("SELECT COUNT(ri) FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId")
    long countByRecipeId(@Param("recipeId") Long recipeId);

    //대체 재료 포함하여 모든 재료와 대체재료 정보 조회 (N+1 문제 해결)
    @Query("SELECT DISTINCT ri FROM RecipeIngredient ri LEFT JOIN FETCH ri.alternatives WHERE ri.recipe.id = :recipeId ORDER BY ri.ingredientOrder")
    List<RecipeIngredient> findByRecipeIdWithAlternativesFetchJoin(@Param("recipeId") Long recipeId);
}