package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.recipe.entity.RecipeImage;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

}
