package novaminds.gradproj.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Follow;
import org.springframework.stereotype.Repository;

import java.util.List;

import static novaminds.gradproj.domain.member.entity.QFollow.follow;
import static novaminds.gradproj.domain.member.entity.QMember.member;
import static novaminds.gradproj.domain.refrigerator.entity.QRefrigerator.refrigerator;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Follow> findFollowersWithMemberByLoginId(String loginId) {
        return queryFactory
                .selectFrom(follow)
                .join(follow.follower, member).fetchJoin()
                .join(member.refrigerator, refrigerator).fetchJoin()
                .where(follow.following.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public List<Follow> findFollowingsWithMemberByLoginId(String loginId) {
        return queryFactory
                .selectFrom(follow)
                .join(follow.following, member).fetchJoin()
                .join(member.refrigerator, refrigerator).fetchJoin()
                .where(follow.follower.loginId.eq(loginId))
                .fetch();
    }
}