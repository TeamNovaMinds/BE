package novaminds.gradproj.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
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

    private BooleanExpression likesAndIdCursorCondition(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        
        // 커서 레시피 정보를 조회하여 likes와 id 기준으로 페이징
        return recipe.likes.lt(
                select(recipe.likes)
                        .from(recipe)
                        .where(recipe.id.eq(cursorId))
        ).or(
                recipe.likes.eq(
                        select(recipe.likes)
                                .from(recipe)
                                .where(recipe.id.eq(cursorId))
                ).and(
                        recipe.id.lt(cursorId)
                )
        );
    }

    @Override
    public List<Recipe> findRecipesByIngredientIds(List<Long> ingredientIds, Long cursorId, int pageSize) {

        // 비어있으면 바로 반환
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return List.of();
        }

        // 2. 레시피 기본 정보만 조회 + 커서 기반 페이징 (좋아요 순 정렬)
        return queryFactory
                .selectFrom(recipe)
                .where(
                        recipe.id.in(
                                select(recipeIngredient.recipe.id)
                                        .from(recipeIngredient)
                                        .where(recipeIngredient.ingredient.id.in(ingredientIds))
                        ),
                        likesAndIdCursorCondition(cursorId)
                )
                .orderBy(recipe.likes.desc(), recipe.id.desc())
                .limit(pageSize)
                .fetch();
    }
}