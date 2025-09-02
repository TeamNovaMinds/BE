package novaminds.gradproj.domain.recipe.repository;

import novaminds.gradproj.domain.recipe.entity.RecipeComment;

import java.util.List;

public interface RecipeCommentRepositoryCustom {
    List<RecipeComment> findParentComments(Long recipeId, Long cursorId, int pageSize);
}
