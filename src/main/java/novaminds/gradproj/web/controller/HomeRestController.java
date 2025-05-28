package novaminds.gradproj.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.security.auth.CurrentLoginId;
import novaminds.gradproj.security.auth.CurrentUser;
import novaminds.gradproj.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
@Tag(name = "Home", description = "홈 화면 보여주는 API, 현재는 로그인 정보 유지 잘 되나 테스트용!!")
public class HomeRestController {

    private final AuthService authService;

    @GetMapping
    public ApiResponse<?> home(@CurrentUser User user) {
        return ApiResponse.onSuccess(
                authService.getProfile(user)
        );
    }

    @GetMapping("/test")
    public ApiResponse<String> getSimpleInfo(@CurrentLoginId String loginId) {
        return ApiResponse.onSuccess("현재 로그인 사용자: " + loginId);
    }
}
