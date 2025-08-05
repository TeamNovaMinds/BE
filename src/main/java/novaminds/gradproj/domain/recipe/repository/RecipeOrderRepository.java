package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.recipe.entity.RecipeOrder;

public interface RecipeOrderRepository extends JpaRepository<RecipeOrder, Long> {
}
