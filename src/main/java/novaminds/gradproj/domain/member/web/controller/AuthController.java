package novaminds.gradproj.domain.member.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.member.service.AuthService;
import novaminds.gradproj.domain.member.service.command.MemberCommandService;
import novaminds.gradproj.domain.member.service.query.MemberQueryService;
import novaminds.gradproj.domain.member.web.dto.MemberRequestDTO;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인/회원가입 관련 API")
public class AuthController {

    private final AuthService authService;
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "회원가입",
            description = "이메일, 비밀번호, 이름, 닉네임, 프로필 이미지, 관심 카테고리를 모두 포함하여 회원가입을 진행합니다. " +
                    "모든 필수 정보를 한 번에 받아 처리하며, 회원가입 완료 시 JWT 토큰이 발급됩니다. " +
                    "isProfileCompleted가 true로 설정되어 바로 서비스 이용이 가능합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ApiResponse<MemberResponseDTO.SignupResponse> signup(
            @Valid @RequestBody MemberRequestDTO.SignupRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.onSuccess(authService.signup(request, response));
    }

    @Operation(summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. " +
                    "isProfileCompleted가 false면 추가 정보 입력 페이지로 이동해야 합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping("/login")
    public ApiResponse<MemberResponseDTO.LoginResponse> login(
            @Valid @RequestBody MemberRequestDTO.LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.onSuccess(authService.login(request, response));
    }

    @Operation(summary = "구글 로그인 리다이렉트",
            description = "구글 로그인 페이지로 리다이렉트하는 API\n" +
                    "리다이렉트해야하므로 swagger에서는 테스트 불가!")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @Operation(summary = "네이버 로그인 리다이렉트",
            description = "네이버 로그인 페이지로 리다이렉트하는 API\n" +
                    "리다이렉트해야하므로 swagger에서는 테스트 불가!")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("/login/naver")
    public void naverLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/naver");
    }

    @Operation(summary = "로그아웃", description = "로그아웃 처리 및 쿠키 삭제")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @Operation(summary = "이메일 중복확인",
            description = "회원가입 시 이메일 중복 여부를 확인합니다. " +
                    "사용 가능한 이메일이면 isSuccess: true를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("/check-email")
    public ApiResponse<String> checkEmailDuplication(
            @RequestParam("email") String email
    ) {
        return ApiResponse.onSuccess(authService.checkEmailDuplication(email));
    }

    @Operation(summary = "닉네임 중복확인",
            description = "닉네임 중복 여부를 확인합니다. " +
                    "사용 가능한 닉네임이면 isSuccess: true를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("/check-nickname")
    public ApiResponse<String> checkNicknameDuplication(
            @RequestParam("nickname") String nickname
    ) {
        return ApiResponse.onSuccess(authService.checkNicknameDuplication(nickname));
    }

    @Operation(summary = "비밀번호 재설정 인증 코드 발송",
            description = "비밀번호를 잊어버렸을 경우, 이메일을 통해 인증 코드 6자리를 전송하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping("/reset-password/send-code")
    public ApiResponse<String> sendResetPasswordToken (
            @Valid @RequestBody MemberRequestDTO.PasswordResetRequest request
    ) {
        return ApiResponse.onSuccess(authService.sendPasswordResetEmail(request.getEmail()));
    }

    @Operation(summary = "비밀번호 재설정",
            description = "이메일로 받은 인증 코드를 검증하고 새로운 비밀번호로 변경합니다. " +
                    "인증 코드는 이메일 발송 후 15분간 유효하며, 일회성으로 사용됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping("/reset-password/confirm")
    public ApiResponse<String> resetPassword(
            @Valid @RequestBody MemberRequestDTO.PasswordResetConfirmRequest request
    ) {
        return ApiResponse.onSuccess(authService.resetPassword(request));
    }

    @Operation(summary = "프로필 이미지 수정",
            description = "로그인한 사용자의 프로필 이미지를 수정합니다. " +
                    "JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PatchMapping("/profile-image")
    public ApiResponse<MemberResponseDTO.UpdateProfileImageResponse> updateProfileImage(
            @CurrentUser Member member,
            @Valid @RequestBody MemberRequestDTO.UpdateProfileImageRequest request
    ) {
        return ApiResponse.onSuccess(memberCommandService.updateProfileImage(member, request));
    }

    @Operation(summary = "내 정보 조회 (캐싱 적용)",
            description = "로그인한 사용자의 정보를 조회합니다. " +
                    "닉네임, 팔로잉 수, 팔로워 수, 포인트, 프로필 이미지 URL을 반환합니다. " +
                    "JWT 토큰이 필요합니다. " +
                    "자주 조회되는 API이므로 5분간 Redis 캐시가 적용됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @GetMapping("/me")
    public ApiResponse<MemberResponseDTO.MyInfoResponse> getMyInfo(
            @CurrentUser Member member
    ) {
        return ApiResponse.onSuccess(memberQueryService.getMyInfo(member));
    }
}