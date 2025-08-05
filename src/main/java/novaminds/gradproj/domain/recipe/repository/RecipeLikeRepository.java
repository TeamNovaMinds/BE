package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import novaminds.gradproj.domain.recipe.entity.RecipeLike;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    //사용자가 특정 레시피에 좋아요를 눌렀는지 확인
    Optional<RecipeLike> findByMemberLoginIdAndRecipeId(String loginId, Long recipeId);

    //사용자가 특정 레시피에 좋아요를 눌렀는지 여부 확인
    boolean existsByMemberLoginIdAndRecipeId(String loginId, Long recipeId);

    //특정 레시피의 좋아요 개수
    long countByRecipeId(Long recipeId);
}
