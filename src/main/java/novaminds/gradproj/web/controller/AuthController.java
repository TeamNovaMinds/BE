package novaminds.gradproj.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.security.auth.CurrentLoginId;
import novaminds.gradproj.security.auth.CurrentUser;
import novaminds.gradproj.security.oauth2.CustomOAuth2UserService;
import novaminds.gradproj.service.AuthService;
import novaminds.gradproj.web.dto.auth.AuthRequest;
import novaminds.gradproj.web.dto.auth.AuthResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인/회원가입 관련 API")
public class AuthController {

    private final AuthService authService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 (기본 정보)",
            description = "이메일, 비밀번호, 이름으로 기본 회원가입을 진행합니다. " +
                    "회원가입 완료 시 JWT 토큰이 발급되며, " +
                    "isProfileCompleted가 false이므로 추가 정보 입력 페이지로 이동해야 합니다.")
    public ApiResponse<AuthResponse.SignupResponse> signup(
            @Valid @RequestBody AuthRequest.SignupRequest request,
            HttpServletResponse response
    ) {
        log.info("🔸 [API 호출] 회원가입 - email: {}", request.getEmail());
        return ApiResponse.onSuccess(authService.signup(request, response));
    }

    @GetMapping("/additional-info")
    @Operation(summary = "추가 정보 입력",
            description = "닉네임과 관심 카테고리(1~3개)를 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    public ApiResponse<?> inputAdditionalInfo(
            @CurrentUser User user
    ) {
        log.info("🔸 [API 호출] 추가 정보 입력 - loginId: {}", user.getLoginId());
        return ApiResponse.onSuccess(customOAuth2UserService.getAdditionalInfoRequirements());
    }

    @PostMapping("/additional-info")
    @Operation(summary = "추가 정보 입력",
            description = "닉네임과 관심 카테고리(1~3개)를 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    public ApiResponse<AuthResponse.AdditionalInfoResponse> completeProfile(
            @CurrentLoginId String loginId,
            @Valid @RequestBody AuthRequest.AdditionalInfoRequest request) {
        log.info("🔸 [API 호출] 추가 정보 입력 - loginId: {}", loginId);
        return ApiResponse.onSuccess(authService.completeProfile(loginId, request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. " +
                    "isProfileCompleted가 false면 추가 정보 입력 페이지로 이동해야 합니다.")
    public ApiResponse<AuthResponse.LoginResponse> login(
            @Valid @RequestBody AuthRequest.LoginRequest request,
            HttpServletResponse response
    ) {
        log.info("🔸 [API 호출] 로그인 - email: {}", request.getEmail());
        return ApiResponse.onSuccess(authService.login(request, response));
    }

    @GetMapping("/login/google")
    @Operation(summary = "구글 로그인 리다이렉트",
            description = "구글 로그인 페이지로 리다이렉트하는 API\n" +
                    "리다이렉트해야하므로 swagger에서는 테스트 불가!")
    public void googleLogin(HttpServletResponse response) throws IOException {
        log.info("🔸 [API 호출] 구글 로그인 리다이렉트");
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        log.info("🔸 [API 호출] 로그아웃");
        authService.logout(response);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        log.info("🔸 [API 호출] 토큰 재발급");
        if (refreshToken == null) {
            throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
        }
        authService.refreshToken(refreshToken, response);
        return ApiResponse.onSuccess("토큰이 재발급되었습니다.");
    }
}