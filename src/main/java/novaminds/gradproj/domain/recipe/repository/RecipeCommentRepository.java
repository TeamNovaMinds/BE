package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import novaminds.gradproj.domain.recipe.entity.RecipeComment;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long>, RecipeCommentRepositoryCustom {

    //댓글 더보기 누르기 전 미리보기로 보여줄 댓글 3개 리스트 (대댓글까지 fetch join)
    @Query("SELECT DISTINCT c " +
            "FROM RecipeComment c " +
            "LEFT JOIN FETCH c.children " +
            "WHERE c.recipe.id = :recipeId " +
                "AND c.parentComment IS NULL " +
            "ORDER BY c.createdAt ASC LIMIT 3")
    List<RecipeComment> findPreviewComments(@Param("recipeId") Long recipeId);

    //특정 레시피의 총 댓글 수 조회 (부모 댓글 + 대댓글)
    @Query("SELECT COUNT(c) " +
            "FROM RecipeComment c " +
            "WHERE c.recipe.id = :recipeId")
    int countCommentsByRecipeId(@Param("recipeId") Long recipeId);

    //댓글 ID들로 작성자 ID만 조회
    @Query(value = "SELECT author_id " +
            "FROM recipe_comments " +
            "WHERE id IN :commentIds",
            nativeQuery = true)
    List<String> findAuthorIdsByCommentIds(@Param("commentIds") List<Long> commentIds);

    //댓글이 특정 레시피에 속하는지 확인
    boolean existsByIdAndRecipeId(Long id, Long recipeId);
}
