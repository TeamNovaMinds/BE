package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import novaminds.gradproj.domain.recipe.entity.RecipeImage;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    // 특정 레시피의 이미지들을 순서대로 조회
    List<RecipeImage> findByRecipeIdOrderByImageOrder(Long recipeId);
}
