package novaminds.gradproj.domain.member.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.command.FollowCommandService;
import novaminds.gradproj.domain.member.service.query.MemberQueryService;
import novaminds.gradproj.domain.member.service.security.auth.CurrentLoginId;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Parameter;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "멤버 프로필 관련", description = "팔로우 및 랭킹 관련 API")
public class MemberController {

    private final FollowCommandService followCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "팔로잉",
            description = "특정 회원을 팔로잉합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{followingNickname}/following")
    public ApiResponse<String> following(
            @CurrentUser Member follower,
            @PathVariable String followingNickname

    ) {
        followCommandService.following(follower, followingNickname);
        return ApiResponse.onSuccess(followingNickname + "님 팔로잉에 성공했습니다.");
    }

    @Operation(summary = "회원 언팔로잉 API",
            description = """
                    팔로잉 목록에서 특정 회원을 언팔로잉 합니다.
                    언팔로잉은 팔로잉 목록에서만 가능합니다. 팔로워 목록에서는 언팔로잉할 수 없습니다.
                    언팔로잉을 하면 해당 회원의 팔로워 목록에서도 제거됩니다.""")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스 입니다.")
    })
    @DeleteMapping("/{followingNickname}/following")
    public ApiResponse<String> unfollowing(
            @CurrentLoginId String followerId,
            @PathVariable String followingNickname

    ) {
        followCommandService.unfollowing(followerId, followingNickname);
        return ApiResponse.onSuccess(followingNickname + "님을 언팔로잉 하였습니다.");
    }

    @Operation(summary = "Top 8 포인트 랭킹 조회 API",
            description = "포인트 상위 8명의 랭킹을 조회합니다. 프로필 이미지, 닉네임, 포인트, 등수 정보를 포함합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/ranking/top8")
    public ApiResponse<MemberResponseDTO.Top8RankingResponse> getTop8Ranking() {
        MemberResponseDTO.Top8RankingResponse response = memberQueryService.getTop8Ranking();
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "전체 포인트 랭킹 조회 API (커서 기반 페이징)",
            description = """
                    전체 사용자의 포인트 랭킹을 커서 기반 페이징으로 조회합니다.
                    무한 스크롤 방식으로 다음 페이지를 불러올 때 nextCursor를 사용합니다.
                    프로필 이미지, 닉네임, 포인트, 등수 정보를 포함합니다.""")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @GetMapping("/ranking/all")
    public ApiResponse<MemberResponseDTO.AllRankingResponse> getAllRanking(
            @Parameter(description = "커서 (다음 페이지 조회시 이전 응답의 nextCursor 값 사용)")
            @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기 (기본값: 20)")
            @RequestParam(defaultValue = "20") int size
    ) {
        MemberResponseDTO.AllRankingResponse response = memberQueryService.getAllRanking(cursor, size);
        return ApiResponse.onSuccess(response);
    }
}
