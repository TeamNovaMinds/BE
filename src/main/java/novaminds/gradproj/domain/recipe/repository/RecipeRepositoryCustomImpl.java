package novaminds.gradproj.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.recipe.entity.RecipeSortType;
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
    public List<Recipe> findRecipes(RecipeCategory category, String keyword, RecipeSortType sortBy, Long cursorId, int pageSize) {
        return switch (sortBy) {
            case LIKES -> findRecipesByLikes(category, keyword, cursorId, pageSize);
            case LATEST -> findRecipesByLatest(category, keyword, cursorId, pageSize);
        };
    }

    private List<Recipe> findRecipesByLatest(RecipeCategory category, String keyword, Long cursorId, int pageSize) {
        return queryFactory
                .selectFrom(recipe)
                .leftJoin(recipe.author).fetchJoin()
                .where(
                        keywordCondition(keyword),
                        categoryCondition(category),
                        cursorCondition(cursorId)
                )
                .orderBy(recipe.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private List<Recipe> findRecipesByLikes(RecipeCategory category, String keyword, Long cursorId, int pageSize) {
        return queryFactory
                .selectFrom(recipe)
                .leftJoin(recipe.author).fetchJoin()
                .where(
                        keywordCondition(keyword),
                        categoryCondition(category),
                        likesAndIdCursorCondition(cursorId)
                )
                .orderBy(recipe.likes.desc(), recipe.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression keywordCondition(String keyword) {

        // 검색어 없으면 모든 레시피 검색
        if (keyword == null || keyword.isBlank()) return null;

        // 검색어에 대해서 띄어쓰기를 전부 없애고 정규화
        String normalizedKeyword = keyword.toLowerCase().replaceAll("\\s+", "");

        // 정규화된 필드와 비교 -> 이때 titleNormalized는 인덱싱돠어있어 검색 속도 빠름
        return recipe.titleNormalized.startsWith(normalizedKeyword);
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

        // 1. 커서 ID로 해당 레시피의 좋아요 개수를 조회
        Integer cursorLikes = queryFactory
                .select(recipe.likes)
                .from(recipe)
                .where(recipe.id.eq(cursorId))
                .fetchOne();

        // 2. 커서에 해당하는 레시피가 없어진 경우(중간에 갑자기 삭제되거나 하는 경우), 항상 false인 조건을 반환.
        if (cursorLikes == null) {
            return Expressions.FALSE;
        }

        // 3. 좋아요 수와 ID를 이용한 커서 조건 생성
        return recipe.likes.lt(cursorLikes)
                .or(recipe.likes.eq(cursorLikes).and(recipe.id.lt(cursorId)));
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