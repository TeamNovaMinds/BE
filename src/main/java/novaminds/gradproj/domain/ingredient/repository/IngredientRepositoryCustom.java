package novaminds.gradproj.domain.ingredient.repository;

import java.util.List;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;

public interface IngredientRepositoryCustom {
	List<Ingredient> searchIngredientsByKeyword(String keyword);

}
