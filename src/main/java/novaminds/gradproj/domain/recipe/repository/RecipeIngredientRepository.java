package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import novaminds.gradproj.domain.recipe.entity.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    //특정 레시피의 재료들을 조회 (재료 정보까지 fetch join)
    @Query("SELECT ri FROM RecipeIngredient ri " +
           "LEFT JOIN FETCH ri.ingredient " +
           "WHERE ri.recipe.id = :recipeId")
    List<RecipeIngredient> findByRecipeIdWithIngredient(@Param("recipeId") Long recipeId);

    // 여러 레시피 ID에 대한 재료들을 배치 조회
    @EntityGraph(attributePaths = {"ingredient"})
    List<RecipeIngredient> findByRecipeIdIn(List<Long> recipeIds);
}