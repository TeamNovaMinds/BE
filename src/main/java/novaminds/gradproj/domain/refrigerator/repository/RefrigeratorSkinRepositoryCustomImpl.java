package novaminds.gradproj.domain.refrigerator.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.refrigerator.entity.QRefrigeratorSkin.refrigeratorSkin;

@Repository
@RequiredArgsConstructor
public class RefrigeratorSkinRepositoryCustomImpl implements RefrigeratorSkinRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RefrigeratorSkin> findSkinsWithCursor(Long cursorId, int pageSize) {
        return queryFactory
                .selectFrom(refrigeratorSkin)
                .where(cursorCondition(cursorId))
                .orderBy(refrigeratorSkin.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId != null ? refrigeratorSkin.id.lt(cursorId) : null;
    }
}