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
        // 페이징 처리된 부모 댓글 ID 목록 조회
        List<Long> parentCommentIds = queryFactory
                .select(recipeComment.id)
                .from(recipeComment)
                .where(
                        recipeComment.recipe.id.eq(recipeId),
                        recipeComment.parentComment.isNull(),
                        cursorCondition(cursorId)
                )
                .orderBy(recipeComment.id.asc())
                .limit(pageSize)
                .fetch();

        // 조회된 ID가 없으면 빈 리스트를 즉시 반환
        if (parentCommentIds.isEmpty()) {
            return List.of();
        }

        // 조회된 ID를 기반으로 실제 데이터 조회 (fetch join 포함)
        return queryFactory
                .selectFrom(recipeComment)
                .distinct()
                .leftJoin(recipeComment.children).fetchJoin()
                .where(recipeComment.id.in(parentCommentIds)) // IN 절로 정확히 필요한 만큼만 조회
                .orderBy(recipeComment.id.asc())
                .fetch();
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId != null ? recipeComment.id.gt(cursorId) : null;
    }
}
