package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.repository.projection.RecipeMainImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

import novaminds.gradproj.domain.recipe.entity.RecipeImage;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    // 특정 레시피의 이미지들을 순서대로 조회
    List<RecipeImage> findByRecipeIdOrderByImageOrder(Long recipeId);

    // 여러 레시피 ID에 대한 대표 이미지 URL을 배치 조회
    @Query("SELECT DISTINCT ri.recipe.id, ri.imageUrl " +
            "FROM RecipeImage ri " +
            "WHERE ri.recipe.id IN :recipeIds " +
            "AND ri.isMain = true")
    List<RecipeMainImage> findMainImageUrlsByRecipeIds(@Param("recipeIds") List<Long> recipeIds);
}
