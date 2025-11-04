package novaminds.gradproj.domain.ingredient.repository;

import static novaminds.gradproj.domain.ingredient.entity.QIngredient.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.entity.IngredientCategory;

@Repository
@RequiredArgsConstructor
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Ingredient> searchIngredients(String keyword, IngredientCategory category) {
		return queryFactory
			.selectFrom(ingredient)
			.where(
				ingredientNameContains(keyword),
				ingredientCategoryEq(category)
			)
			.fetch();
	}

	private BooleanExpression ingredientNameContains(String keyword) {
		if (!StringUtils.hasText(keyword)) {
			return null;
		}

		return ingredient.ingredientNameNormalized.startsWith(keyword);
	}

	private BooleanExpression ingredientCategoryEq(IngredientCategory category) {
		return category != null ? ingredient.ingredientCategory.eq(category) : null;
	}

}
