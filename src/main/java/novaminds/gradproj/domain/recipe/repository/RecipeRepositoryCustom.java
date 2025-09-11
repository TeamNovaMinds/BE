package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;

import java.util.List;

public interface RecipeRepositoryCustom {
    
    List<Recipe> findRecipes(RecipeCategory category, Long cursorId, int pageSize);
    
    // 재료 ID로 레시피 조회 + 페이징 (좋아요 순 정렬)
    List<Recipe> findRecipesByIngredientIds(List<Long> ingredientIds, Long cursorId, int pageSize);
}
