package novaminds.gradproj.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
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

    @PostMapping("/signup")
    public ApiResponse<AuthResponse.SignupResponse> signup(@Valid @RequestBody AuthRequest.SignupRequest request) {
        log.info("🔸 [API 호출] 회원가입 - email: {}", request.getEmail());
        return ApiResponse.onSuccess(authService.signup(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse.LoginResponse> login(
            @Valid @RequestBody AuthRequest.LoginRequest request,
            HttpServletResponse response) {
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
            HttpServletResponse response) {
        log.info("🔸 [API 호출] 토큰 재발급");
        if (refreshToken == null) {
            throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
        }
        authService.refreshToken(refreshToken, response);
        return ApiResponse.onSuccess("토큰이 재발급되었습니다.");
    }
}