package novaminds.gradproj.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.recipe.entity.QRecipe.recipe;
import static novaminds.gradproj.domain.recipe.entity.QRecipeIngredient.recipeIngredient;

@Repository
@RequiredArgsConstructor
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<Recipe> findRecipes(RecipeCategory category, Long cursorId, int pageSize) {
        
        return queryFactory
                .selectFrom(recipe)
                .leftJoin(recipe.author).fetchJoin()
                .where(
                        categoryCondition(category),
                        cursorCondition(cursorId)
                )
                .orderBy(recipe.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression categoryCondition(RecipeCategory category) {
        return category != null ? recipe.recipeCategory.eq(category) : null;
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId != null ? recipe.id.lt(cursorId) : null;
    }

    @Override
    public List<Recipe> findRecipesByIngredientIds(List<Long> ingredientIds, Long cursorId, int pageSize) {
        // 1. 재료 ID로 레시피 ID 목록 조회
        List<Long> recipeIds = queryFactory
                .select(recipeIngredient.recipe.id)
                .from(recipeIngredient)
                .where(recipeIngredient.ingredient.id.in(ingredientIds))
                .distinct()
                .fetch();

        if (recipeIds.isEmpty()) {
            return List.of();
        }

        // 2. 레시피 기본 정보만 조회 + 커서 기반 페이징 (좋아요 순 정렬)
        return queryFactory
                .selectFrom(recipe)
                .where(
                    recipe.id.in(recipeIds),
                    cursorCondition(cursorId)
                )
                .orderBy(recipe.likes.desc(), recipe.id.desc())
                .limit(pageSize)
                .fetch();
    }
}