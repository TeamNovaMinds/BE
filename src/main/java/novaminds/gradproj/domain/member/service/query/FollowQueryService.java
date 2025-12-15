package novaminds.gradproj.domain.member.service.query;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Follow;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.FollowRepository;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorInvitationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FollowQueryService {

    private final FollowRepository followRepository;
    private final RefrigeratorInvitationRepository refrigeratorInvitationRepository;

    public MemberResponseDTO.FollowersResponse getFollowers(Member currentMember) {
        // 나를 팔로우하는 사람들 조회 (fetch join으로 N+1 방지)
        List<Follow> followers = followRepository.findFollowersWithMemberByLoginId(currentMember.getLoginId());

        // 내가 팔로우하는 사람들의 loginId를 Set으로 (맞팔 확인용)
        Set<String> myFollowingIds = followRepository.findByFollowerLoginId(currentMember.getLoginId())
                .stream()
                .map(follow -> follow.getFollowing().getLoginId())
                .collect(Collectors.toSet());

        // 나의 냉장고 ID
        Long myRefrigeratorId = currentMember.getRefrigerator().getId();

        // 팔로워 멤버 리스트
        List<Member> followerMembers = followers.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());

        // 내가 보낸 PENDING 초대 조회 (batch)
        List<RefrigeratorInvitation> pendingInvitations = refrigeratorInvitationRepository
                .findByInviterAndInviteeInAndStatus(
                        currentMember,
                        followerMembers,
                        RefrigeratorInvitation.InvitationStatus.PENDING
                );

        // PENDING 초대를 받은 사람들의 loginId를 Set으로
        Set<String> pendingInviteeIds = pendingInvitations.stream()
                .map(invitation -> invitation.getInvitee().getLoginId())
                .collect(Collectors.toSet());

        // DTO 변환
        List<MemberResponseDTO.FollowMemberInfo> followerInfos = followers.stream()
                .map(follow -> {
                    Member followerMember = follow.getFollower();
                    MemberResponseDTO.FollowMemberInfo.InvitationStatus status =
                            determineInvitationStatus(
                                    followerMember,
                                    myFollowingIds,
                                    myRefrigeratorId,
                                    pendingInviteeIds
                            );

                    return MemberResponseDTO.FollowMemberInfo.builder()
                            .nickname(followerMember.getNickname())
                            .profileImgUrl(followerMember.getProfileImage())
                            .invitationStatus(status)
                            .build();
                })
                .collect(Collectors.toList());

        return MemberResponseDTO.FollowersResponse.builder()
                .followers(followerInfos)
                .build();
    }

    public MemberResponseDTO.FollowingsResponse getFollowings(Member currentMember) {
        // 내가 팔로우하는 사람들 조회 (fetch join으로 N+1 방지)
        List<Follow> followings = followRepository.findFollowingsWithMemberByLoginId(currentMember.getLoginId());

        // 나를 팔로우하는 사람들의 loginId를 Set으로 (맞팔 확인용)
        Set<String> myFollowerIds = followRepository.findByFollowingLoginId(currentMember.getLoginId())
                .stream()
                .map(follow -> follow.getFollower().getLoginId())
                .collect(Collectors.toSet());

        // 나의 냉장고 ID
        Long myRefrigeratorId = currentMember.getRefrigerator().getId();

        // 팔로잉 멤버 리스트
        List<Member> followingMembers = followings.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        // 내가 보낸 PENDING 초대 조회 (batch)
        List<RefrigeratorInvitation> pendingInvitations = refrigeratorInvitationRepository
                .findByInviterAndInviteeInAndStatus(
                        currentMember,
                        followingMembers,
                        RefrigeratorInvitation.InvitationStatus.PENDING
                );

        // PENDING 초대를 받은 사람들의 loginId를 Set으로
        Set<String> pendingInviteeIds = pendingInvitations.stream()
                .map(invitation -> invitation.getInvitee().getLoginId())
                .collect(Collectors.toSet());

        // DTO 변환
        List<MemberResponseDTO.FollowMemberInfo> followingInfos = followings.stream()
                .map(follow -> {
                    Member followingMember = follow.getFollowing();
                    MemberResponseDTO.FollowMemberInfo.InvitationStatus status =
                            determineInvitationStatus(
                                    followingMember,
                                    myFollowerIds,
                                    myRefrigeratorId,
                                    pendingInviteeIds
                            );

                    return MemberResponseDTO.FollowMemberInfo.builder()
                            .nickname(followingMember.getNickname())
                            .profileImgUrl(followingMember.getProfileImage())
                            .invitationStatus(status)
                            .build();
                })
                .collect(Collectors.toList());

        return MemberResponseDTO.FollowingsResponse.builder()
                .followings(followingInfos)
                .build();
    }

    /**
     * 초대 상태를 결정하는 메서드
     *
     * @param targetMember 대상 멤버
     * @param mutualCheckSet 맞팔 확인을 위한 Set (팔로워 목록이면 내 팔로잉 Set, 팔로잉 목록이면 내 팔로워 Set)
     * @param myRefrigeratorId 나의 냉장고 ID
     * @param pendingInviteeIds PENDING 상태 초대를 받은 사람들의 loginId Set
     * @return InvitationStatus
     */
    private MemberResponseDTO.FollowMemberInfo.InvitationStatus determineInvitationStatus(
            Member targetMember,
            Set<String> mutualCheckSet,
            Long myRefrigeratorId,
            Set<String> pendingInviteeIds
    ) {
        // 맞팔이 아니면 NOT_MUTUAL
        if (!mutualCheckSet.contains(targetMember.getLoginId())) {
            return MemberResponseDTO.FollowMemberInfo.InvitationStatus.NOT_MUTUAL;
        }

        // 맞팔이면 추가 조건 확인
        // 1. 같은 냉장고 사용 중인지 확인
        if (targetMember.getRefrigerator().getId().equals(myRefrigeratorId)) {
            return MemberResponseDTO.FollowMemberInfo.InvitationStatus.ALREADY_SAME_REFRIGERATOR;
        }

        // 2. 이미 초대장 보냈는지 확인
        if (pendingInviteeIds.contains(targetMember.getLoginId())) {
            return MemberResponseDTO.FollowMemberInfo.InvitationStatus.INVITATION_PENDING;
        }

        // 3. 둘 다 아니면 초대 가능
        return MemberResponseDTO.FollowMemberInfo.InvitationStatus.MUTUAL_FOLLOW_INVITE;
    }
}