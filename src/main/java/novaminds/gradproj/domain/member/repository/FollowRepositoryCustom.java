package novaminds.gradproj.domain.member.repository;

import novaminds.gradproj.domain.member.entity.Follow;

import java.util.List;

public interface FollowRepositoryCustom {

    // 팔로워 목록 조회 (N+1 방지를 위한 fetch join)
    List<Follow> findFollowersWithMemberByLoginId(String loginId);

    // 팔로잉 목록 조회 (N+1 방지를 위한 fetch join)
    List<Follow> findFollowingsWithMemberByLoginId(String loginId);
}