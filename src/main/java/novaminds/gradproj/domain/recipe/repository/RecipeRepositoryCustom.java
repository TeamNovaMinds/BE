package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.recipe.entity.RecipeSortType;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> findRecipes(RecipeCategory category, String keyword, RecipeSortType sortBy, Long cursorId, int pageSize);

    // 재료 ID로 레시피 조회 + 페이징 (좋아요 순 정렬)
    List<Recipe> findRecipesByIngredientIds(List<Long> ingredientIds, Long cursorId, int pageSize);

    // 내가 작성한 레시피 조회 + 페이징 + 정렬
    List<Recipe> findMyRecipes(String authorLoginId, RecipeSortType sortBy, Long cursorId, int pageSize);

    // 내가 좋아요 누른 레시피 조회 + 페이징 + 정렬
    List<Recipe> findLikedRecipes(String memberLoginId, RecipeSortType sortBy, Long cursorId, int pageSize);
}
