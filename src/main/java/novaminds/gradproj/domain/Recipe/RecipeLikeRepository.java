package novaminds.gradproj.domain.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    //사용자가 특정 레시피에 좋아요를 눌렀는지 확인
    Optional<RecipeLike> findByUserLoginIdAndRecipeId(String loginId, Long recipeId);

    //사용자가 특정 레시피에 좋아요를 눌렀는지 여부 확인
    boolean existsByUserLoginIdAndRecipeId(String loginId, Long recipeId);

    //특정 레시피의 좋아요 개수
    long countByRecipeId(Long recipeId);
}
