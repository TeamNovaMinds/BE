package novaminds.gradproj.domain.recipe.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.recipe.entity.RecipeComment;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.recipe.entity.QRecipeComment.recipeComment;

@Repository
@RequiredArgsConstructor
public class RecipeCommentRepositoryCustomImpl implements RecipeCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecipeComment> findParentComments(Long recipeId, Long cursorId, int pageSize) {
        return queryFactory
                .selectFrom(recipeComment)
                .leftJoin(recipeComment.children).fetchJoin() // 대댓글까지 fetch join
                .where(
                        recipeComment.recipe.id.eq(recipeId),
                        recipeComment.parentComment.isNull(), // 부모 댓글만 조회
                        cursorCondition(cursorId)
                )
                .orderBy(recipeComment.id.asc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId != null ? recipeComment.id.gt(cursorId) : null;
    }
}
