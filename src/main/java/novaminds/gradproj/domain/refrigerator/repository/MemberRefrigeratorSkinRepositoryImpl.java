package novaminds.gradproj.domain.refrigerator.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.refrigerator.entity.QMemberRefrigeratorSkin.memberRefrigeratorSkin;

@Repository
@RequiredArgsConstructor
public class MemberRefrigeratorSkinRepositoryImpl implements MemberRefrigeratorSkinRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberRefrigeratorSkin> findOwnedSkinsWithCursor(String memberId, Long cursorId, int pageSize) {
        return queryFactory
                .selectFrom(memberRefrigeratorSkin)
                .where(
                        memberRefrigeratorSkin.member.loginId.eq(memberId),
                        cursorCondition(cursorId)
                )
                .orderBy(memberRefrigeratorSkin.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression cursorCondition(Long cursorId) {
        return cursorId != null ? memberRefrigeratorSkin.id.lt(cursorId) : null;
    }
}