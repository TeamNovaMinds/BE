package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import novaminds.gradproj.domain.recipe.entity.RecipeLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike, Long> {
    //사용자가 특정 레시피에 좋아요를 눌렀는지 확인
    Optional<RecipeLike> findByMemberLoginIdAndRecipeId(String loginId, Long recipeId);

    //사용자가 특정 레시피에 좋아요를 눌렀는지 여부 확인
    boolean existsByMemberLoginIdAndRecipeId(String loginId, Long recipeId);

    /**
     * 여러 레시피 ID에 대해 현재 사용자가 좋아요를 누른 레시피 ID 목록을 조회합니다.
     *
     * @param memberId  현재 사용자의 ID
     * @param recipeIds 확인할 레시피 ID 목록
     * @return 좋아요를 누른 레시피 ID의 Set
     */
    @Query("SELECT rl.recipe.id " +
            "FROM RecipeLike rl " +
            "WHERE rl.member.loginId = :memberId " +
                "AND rl.recipe.id IN :recipeIds")
    Set<Long> findLikedRecipeIdsByMemberIdAndRecipeIds(@Param("memberId") String memberId, @Param("recipeIds") List<Long> recipeIds);
}
