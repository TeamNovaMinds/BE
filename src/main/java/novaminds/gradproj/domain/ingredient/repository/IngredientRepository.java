package novaminds.gradproj.domain.ingredient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, IngredientRepositoryCustom {

	//전체 조회
	//List<Ingredient> findAll();

	//검색
	//List<Ingredient> findByIngredientNameContainingIgnoreCase(String keyword);
}
