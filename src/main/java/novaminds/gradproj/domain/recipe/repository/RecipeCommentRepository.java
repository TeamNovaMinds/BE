package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.repository.projection.CommentAuthorInfo;
import novaminds.gradproj.domain.recipe.repository.projection.RecipeCommentCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import novaminds.gradproj.domain.recipe.entity.RecipeComment;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long>, RecipeCommentRepositoryCustom {

    //댓글 더보기 누르기 전 미리보기로 보여줄 댓글 3개 리스트
    List<RecipeComment> findTop3ByRecipeIdAndParentCommentIsNullOrderByCreatedAtAsc(Long recipeId);

    //특정 레시피의 총 댓글 수 조회 (부모 댓글 + 대댓글)
    @Query("SELECT COUNT(c) " +
            "FROM RecipeComment c " +
            "WHERE c.recipe.id = :recipeId")
    Long countCommentsByRecipeId(@Param("recipeId") Long recipeId);

    //댓글이 특정 레시피에 속하는지 확인
    boolean existsByIdAndRecipeId(Long id, Long recipeId);

    // 여러 레시피 ID에 대한 댓글 수를 배치 조회
    @Query("""
            SELECT
            c.recipe.id AS recipeId,
            COUNT(c) AS cnt
            FROM RecipeComment c
            WHERE c.recipe.id IN :recipeIds
            GROUP BY c.recipe.id
            """)
    List<RecipeCommentCount> countCommentsByRecipeIds(@Param("recipeIds") List<Long> recipeIds);

    // 댓글 ID와 작성자 ID를 함께 조회 - c.author.loginId 에서 N+1 문제 발생 XXX -> JPA가 효율적인 JOIN으로 문제 해결
    @Query("""
            SELECT
            c.id AS id,
            c.author.loginId AS authorId
            FROM RecipeComment c
            WHERE c.id IN :commentIds
            """)
    List<CommentAuthorInfo> findCommentIdAndAuthorId(@Param("commentIds") List<Long> commentIds);
}
