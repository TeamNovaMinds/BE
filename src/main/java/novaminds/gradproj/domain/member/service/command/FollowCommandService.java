package novaminds.gradproj.domain.member.service.command;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Follow;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.FollowRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowCommandService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Caching(evict = {
            @CacheEvict(value = "memberInfo", key = "#follower.loginId"),
            @CacheEvict(value = "memberInfo", key = "#result.loginId")
    })
    public Member following(Member follower, String followingNickName) {

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

        return following;
    }

    @Caching(evict = {
            @CacheEvict(value = "memberInfo", key = "#followerId"),
            @CacheEvict(value = "memberInfo", key = "#result")
    })
    public String unfollowing(String followerId, String followingNickname) {

        String followingId = memberRepository.findIdByNickname(followingNickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!followRepository.existsByFollowerLoginIdAndFollowingLoginId(followerId, followingId)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOLLOWING);
        }

        followRepository.deleteByFollowerLoginIdAndFollowingLoginId(followerId, followingId);

        return followingId;
    }
}
