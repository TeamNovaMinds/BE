package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import novaminds.gradproj.domain.recipe.entity.RecipeOrder;

public interface RecipeOrderRepository extends JpaRepository<RecipeOrder, Long> {

    // 특정 레시피의 조리 순서들을 순서대로 조회
    List<RecipeOrder> findByRecipeIdOrderByOrder(Long recipeId);
}
