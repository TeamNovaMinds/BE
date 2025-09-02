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

    @Operation(summary = "회원가입 (기본 정보)",
            description = "이메일, 비밀번호, 이름으로 기본 회원가입을 진행합니다. " +
                    "회원가입 완료 시 JWT 토큰이 발급되며, " +
                    "isProfileCompleted가 false이므로 추가 정보 입력 페이지로 이동해야 합니다.")
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


    @Operation(summary = "추가 정보 입력",
            description = "프로필 이미지와 닉네임을 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping(value = "/additional-info-part1")
    public ApiResponse<MemberResponseDTO.AdditionalInfoResponse> completeProfile(
            @CurrentUser Member member,
            @Valid @RequestBody MemberRequestDTO.AdditionalInfoNicknameRequest request
    ) {
        return ApiResponse.onSuccess(authService.completeProfilePart1(member, request));
    }

    @Operation(summary = "추가 정보 입력",
            description = "관심 카테고리(1~3개)를 입력합니다. " +
                    "JWT 토큰이 필요합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping(value = "/additional-info-part2")
    public ApiResponse<MemberResponseDTO.AdditionalInfoResponse> completeProfile(
            @CurrentUser Member member,
            @Valid @RequestBody MemberRequestDTO.AdditionalInfoInterestRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.onSuccess(authService.completeProfilePart2(member, request, response));
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

    @Operation(summary = "이메일로 비밀번호 재설정",
            description = "비밀번호를 잊어버렸을 경우, 이메일을 통해 인증 코드 6자리를 전송하는 api 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @PostMapping("reset-password")
    public ApiResponse<String> sendResetPasswordToken (
            @Valid @RequestBody MemberRequestDTO.PasswordResetRequest request
    ) {
        return ApiResponse.onSuccess(authService.sendPasswordResetEmail(request));
    }
}