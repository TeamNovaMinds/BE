package novaminds.gradproj.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.security.auth.PrincipalDetails;
import novaminds.gradproj.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public ApiResponse<?> home(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ApiResponse.onSuccess(
                authService.getProfile(principalDetails.getUsername())
        );
    }
}
