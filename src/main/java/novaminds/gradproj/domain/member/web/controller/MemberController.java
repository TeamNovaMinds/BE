package novaminds.gradproj.domain.member.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.command.FollowCommandService;
import novaminds.gradproj.domain.member.service.security.auth.CurrentLoginId;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "멤버 프로필 관련", description = "팔로우 관련 API")
public class MemberController {

    private final FollowCommandService followCommandService;

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
}
