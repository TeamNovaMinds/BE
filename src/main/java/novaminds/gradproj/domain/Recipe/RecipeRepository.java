package novaminds.gradproj.domain.Recipe;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	@EntityGraph(attributePaths = {"author", "recipeImages"})
	Optional<Recipe> findById(Long recipeId);
}
