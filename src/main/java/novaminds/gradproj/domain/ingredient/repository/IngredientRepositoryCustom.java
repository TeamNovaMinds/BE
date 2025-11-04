package novaminds.gradproj.domain.ingredient.repository;

import java.util.List;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.entity.IngredientCategory;

public interface IngredientRepositoryCustom {
	List<Ingredient> searchIngredients(String keyword, IngredientCategory category);

}
