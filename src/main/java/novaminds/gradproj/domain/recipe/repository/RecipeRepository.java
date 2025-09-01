package novaminds.gradproj.domain.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.recipe.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    @EntityGraph(attributePaths = {"author"})
    Optional<Recipe> findRecipeWithAuthorById(Long recipeId);
}
