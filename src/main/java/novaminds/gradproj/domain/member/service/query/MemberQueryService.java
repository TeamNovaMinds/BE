package novaminds.gradproj.domain.member.service.query;

import novaminds.gradproj.domain.member.converter.MemberConverter;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.FollowRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.repository.projection.AuthorInfoProjection;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    /**
     * 여러 회원의 ID를 받아 AuthorInfo DTO 맵을 반환합니다. (배치 조회)
     * 팔로잉 여부와 본인 여부를 포함합니다.
     *
     * @param currentMemberId 현재 조회하는 회원 ID (null일 경우 following, myself는 모두 false)
     * @param authorIds 조회할 작성자 ID 목록
     * @return Key: 회원 ID, Value: AuthorInfo DTO
     */
    public Map<String, MemberResponseDTO.AuthorInfo> getAuthorInfoMap(String currentMemberId, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return Map.of();
        }

        // 작성자 정보 배치 조회
        List<AuthorInfoProjection> authorInfos = memberRepository.findAuthorInfoByLoginIds(authorIds);

        // 현재 사용자가 팔로우하는 사람들의 ID 조회
        Set<String> followingIds = new HashSet<>();
        if (currentMemberId != null) {
            followingIds = followRepository.findByFollowerLoginId(currentMemberId).stream()
                    .map(follow -> follow.getFollowing().getLoginId())
                    .collect(Collectors.toSet());
        }

        // 최종 followingIds를 final 변수로 캡처
        final Set<String> finalFollowingIds = followingIds;

        return authorInfos.stream()
                .collect(Collectors.toMap(
                        AuthorInfoProjection::getLoginId,
                        proj -> {
                            boolean following = currentMemberId != null && finalFollowingIds.contains(proj.getLoginId());
                            boolean myself = currentMemberId != null && currentMemberId.equals(proj.getLoginId());
                            return MemberConverter.toAuthorInfo(
                                    proj.getNickname(),
                                    proj.getProfileImage(),
                                    following,
                                    myself
                            );
                        }
                ));
    }

    /**
     * 내 정보를 조회합니다. (캐싱 적용)
     * @param member 현재 로그인한 사용자
     * @return 내 정보 응답 DTO
     */
    @Cacheable(value = "memberInfo", key = "#member.loginId")
    public MemberResponseDTO.MyInfoResponse getMyInfo(Member member) {
        return MemberResponseDTO.MyInfoResponse.from(member);
    }

    /**
     * Top 8 포인트 랭킹을 조회합니다.
     * @return Top 8 랭킹 응답 DTO
     */
    public MemberResponseDTO.Top8RankingResponse getTop8Ranking() {
        Pageable pageable = PageRequest.of(0, 8);
        List<Member> topMembers = memberRepository.findTop8ByOrderByPointDescCreatedAtAsc(pageable);

        List<MemberResponseDTO.PointRankingResponse> rankings = topMembers.stream()
                .map(member -> {
                    long rank = memberRepository.countMembersWithHigherPoint(member.getPoint());
                    return MemberResponseDTO.PointRankingResponse.builder()
                            .rank(rank)
                            .nickname(member.getNickname())
                            .profileImgUrl(member.getProfileImage())
                            .point(member.getPoint())
                            .build();
                })
                .collect(Collectors.toList());

        return MemberResponseDTO.Top8RankingResponse.builder()
                .rankings(rankings)
                .build();
    }

    /**
     * 전체 포인트 랭킹을 커서 기반 페이징으로 조회합니다.
     * @param cursor 커서 (Base64 인코딩된 "point:createdAt" 형식)
     * @param size 페이지 크기
     * @return 전체 랭킹 응답 DTO
     */
    public MemberResponseDTO.AllRankingResponse getAllRanking(String cursor, int size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        List<Member> members;

        if (cursor == null || cursor.isEmpty()) {
            // 첫 페이지 조회
            members = memberRepository.findTop8ByOrderByPointDescCreatedAtAsc(pageable);
        } else {
            // 커서 디코딩
            String decodedCursor = new String(Base64.getDecoder().decode(cursor));
            String[] parts = decodedCursor.split(":");
            Integer cursorPoint = Integer.parseInt(parts[0]);
            LocalDateTime cursorCreatedAt = LocalDateTime.parse(parts[1]);

            members = memberRepository.findRankingWithCursor(cursorPoint, cursorCreatedAt, pageable);
        }

        // 다음 페이지 존재 여부 확인
        boolean hasNext = members.size() > size;
        if (hasNext) {
            members = members.subList(0, size);
        }

        // 등수 계산 및 DTO 변환
        List<MemberResponseDTO.PointRankingResponse> rankings = members.stream()
                .map(member -> {
                    long rank = memberRepository.countMembersWithHigherPoint(member.getPoint());
                    return MemberResponseDTO.PointRankingResponse.builder()
                            .rank(rank)
                            .nickname(member.getNickname())
                            .profileImgUrl(member.getProfileImage())
                            .point(member.getPoint())
                            .build();
                })
                .collect(Collectors.toList());

        // 다음 커서 생성
        String nextCursor = null;
        if (hasNext && !members.isEmpty()) {
            Member lastMember = members.get(members.size() - 1);
            String cursorString = lastMember.getPoint() + ":" + lastMember.getCreatedAt().toString();
            nextCursor = Base64.getEncoder().encodeToString(cursorString.getBytes());
        }

        return MemberResponseDTO.AllRankingResponse.builder()
                .rankings(rankings)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
