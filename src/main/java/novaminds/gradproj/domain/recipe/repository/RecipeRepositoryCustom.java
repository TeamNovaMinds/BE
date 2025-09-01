package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;

import java.util.List;

public interface RecipeRepositoryCustom {
    
    List<Recipe> findRecipes(RecipeCategory category, Long cursorId, int pageSize);
}
