package novaminds.gradproj.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.user.QUser;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.security.auth.CurrentLoginId;
import novaminds.gradproj.security.auth.CurrentUser;
import novaminds.gradproj.security.oauth2.CustomOAuth2UserService;
import novaminds.gradproj.service.AuthService;
import novaminds.gradproj.web.dto.auth.AuthRequest;
import novaminds.gradproj.web.dto.auth.AuthResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인/회원가입 관련 API")
public class AuthRestController {

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
            description = "프로필 이미지와 닉네임과 관심 카테고리(1~3개)를 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    public ApiResponse<?> inputAdditionalInfo(
            @CurrentUser User user
    ) {
        log.info("🔸 [API 호출] 추가 정보 입력 - loginId: {}", user.getLoginId());
        return ApiResponse.onSuccess(customOAuth2UserService.getAdditionalInfoRequirements());
    }

/*    @Operation(
            summary = "추가 정보 입력",
            description = "닉네임, 관심 카테고리, 프로필 이미지 업로드",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = AuthRequest.AdditionalInfoRequest.class))
            )
    )*/
    @PostMapping(value = "/additional-info-part1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "추가 정보 입력",
            description = "프로필 이미지와 닉네임을 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    public ApiResponse<AuthResponse.AdditionalInfoResponse> completeProfile(
            @CurrentUser User user,
            @Valid @RequestPart("data") AuthRequest.AdditionalInfoNicknameRequest request,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg
    ) {
        log.info("🔸 [API 호출] 추가 정보 입력 (이미지 포함) - loginId: {}, 이미지: {}",
                user, profileImg != null ? profileImg.getOriginalFilename() : "없음");
        return ApiResponse.onSuccess(authService.completeProfilePart1(user, request, profileImg));
    }

    @PostMapping(value = "/additional-info-part2")
    @Operation(summary = "추가 정보 입력",
            description = "관심 카테고리(1~3개)를 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    public ApiResponse<AuthResponse.AdditionalInfoResponse> completeProfile(
            @CurrentUser User user,
            @Valid @RequestBody AuthRequest.AdditionalInfoInterestRequest request
    ) {
        return ApiResponse.onSuccess(authService.completeProfilePart2(user, request));
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

    @GetMapping("/login/naver")
    @Operation(summary = "네이버 로그인 리다이렉트",
            description = "네이버 로그인 페이지로 리다이렉트하는 API\n" +
                    "리다이렉트해야하므로 swagger에서는 테스트 불가!")
    public void naverLogin(HttpServletResponse response) throws IOException {
        log.info("🔸 [API 호출] 네이버 로그인 리다이렉트");
        response.sendRedirect("/oauth2/authorization/naver");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        log.info("🔸 [API 호출] 로그아웃");
        authService.logout(response);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복확인",
            description = "회원가입 시 이메일 중복 여부를 확인합니다. " +
                    "사용 가능한 이메일이면 isSuccess: true를 반환합니다.")
    public ApiResponse<String> checkEmailDuplication(
            @RequestParam("email") String email
    ) {
        log.info("🔸 [API 호출] 이메일 중복확인 - email: {}", email);
        return ApiResponse.onSuccess(authService.checkEmailDuplication(email));
    }

    @GetMapping("reset-password")
    @Operation(summary = "이메일로 비밀번호 재설정",
            description = "비밀번호를 잊어버렸을 경우, 이메일을 통해 인증 코드 6자리를 전송하는 api 입니다.")
    public ApiResponse<String> sendResetPasswordToken (
            @RequestParam("email") String email
    ) {
        //테스트용
        log.info("🔸 [API 호출] 비밀번호 재설정 이메일 전송 - email: {}", email);
        return ApiResponse.onSuccess(authService.sendPasswordResetEmail(email));
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