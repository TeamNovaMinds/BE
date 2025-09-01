package novaminds.gradproj.domain.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

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
    
    // 여러 레시피 ID에 대한 댓글 수를 배치 조회 - JPQL은 Map<> 으로 반환 지원
    @Query("SELECT c.recipe.id, COUNT(c) " +
           "FROM RecipeComment c " +
           "WHERE c.recipe.id IN :recipeIds " +
           "GROUP BY c.recipe.id")
    Map<Long, Long> countCommentsByRecipeIds(@Param("recipeIds") List<Long> recipeIds);

    // 댓글 ID와 작성자 ID를 함께 조회 - 네이티브 쿼리는 Map<> 변환 지원 X
    @Query(value = "SELECT id, author_id FROM recipe_comments WHERE id IN :commentIds", nativeQuery = true)
    List<Object[]> findCommentIdAndAuthorId(@Param("commentIds") List<Long> commentIds);
}
