package novaminds.gradproj.domain.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    //특정 레시피의 댓글 목록 (페이징)
    @EntityGraph(attributePaths = {"author", "children", "children.author"})
    Page<RecipeComment> findByRecipeIdAndParentCommentIsNull(Long recipeId, Pageable pageable);

    //특정 댓글의 대댓글 목록
    List<RecipeComment> findByParentCommentId(Long parentCommentId);

    //댓글 더보기 누르기 전 미리보기로 보여줄 댓글 몇 개 리스트
    @EntityGraph(attributePaths = {"author", "children", "children.author"})
    List<RecipeComment> findTop3ByRecipeIdAndParentCommentIsNullOrderByCreatedAtDesc(Long recipeId);

    //특정 레시피의 댓글 개수
    long countByRecipeId(Long recipeId);

    //특정 사용자의 댓글 목록
    Page<RecipeComment> findByAuthorLoginId(String loginId, Pageable pageable);
}
