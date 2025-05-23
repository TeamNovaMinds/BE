package novaminds.gradproj.domain.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    //특정 레시피의 댓글 목록 (페이징)
    Page<RecipeComment> findByRecipeIdAndParentCommentIsNull(Long recipeId, Pageable pageable);

    //특정 댓글의 대댓글 목록
    List<RecipeComment> findByParentCommentId(Long parentCommentId);

    //특정 레시피의 댓글 개수
    long countByRecipeId(Long recipeId);

    //특정 사용자의 댓글 목록
    Page<RecipeComment> findByAuthorLoginId(String loginId, Pageable pageable);
}
