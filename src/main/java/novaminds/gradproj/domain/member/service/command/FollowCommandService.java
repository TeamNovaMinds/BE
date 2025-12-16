package novaminds.gradproj.domain.member.service.command;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Follow;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.FollowRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowCommandService {

    private final CacheManager cacheManager;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void following(Member follower, String followingNickName) {

        Member following = memberRepository.findByNickname(followingNickName)
                .orElseThrow(
                        () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
                );

        if (follower.getLoginId().equals(following.getLoginId())) {
            throw new GeneralException(ErrorStatus.MEMBER_CANNOT_FOLLOW_SELF);
        }

        if (followRepository.existsByFollowerLoginIdAndFollowingLoginId(follower.getLoginId(), following.getLoginId())) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        evictMemberCache(follower.getLoginId());
        evictMemberCache(following.getLoginId());
    }

    public void unfollowing(String followerId, String followingNickname) {

        String followingId = memberRepository.findIdByNickname(followingNickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!followRepository.existsByFollowerLoginIdAndFollowingLoginId(followerId, followingId)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOLLOWING);
        }

        followRepository.deleteByFollowerLoginIdAndFollowingLoginId(followerId, followingId);

        evictMemberCache(followerId);
        evictMemberCache(followingId);
    }

    private void evictMemberCache(String loginId) {
        // "memberInfo"라는 이름의 캐시 저장소를 가져와서 해당 loginId 키를 삭제
        Objects.requireNonNull(cacheManager.getCache("memberInfo")).evict(loginId);
    }
}
