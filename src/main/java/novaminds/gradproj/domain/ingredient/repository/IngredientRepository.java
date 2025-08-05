package novaminds.gradproj.domain.ingredient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
