package novaminds.gradproj.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.recipe.entity.QRecipe;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.recipe.entity.QRecipe.recipe;

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
}