package novaminds.gradproj.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 특정 유저'가' 팔로우하는 사람들
    List<Follow> findByFollowerLoginId(String followerLoginId);

    // 특정 유저'를' 팔로우하는 사람들
    List<Follow> findByFollowingLoginId(String followingLoginId);

    // 팔로워 수
    long countByFollowingLoginId(String followingLoginId);

    // 팔로잉 수
    long countByFollowerLoginId(String followerLoginId);
}