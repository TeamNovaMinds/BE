package novaminds.gradproj.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.security.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("🔄 [OAuth2 로그인 성공] 사용자: {}", authentication.getName());

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // HTTP Only Cookie로 설정
        response.addCookie(jwtTokenProvider.createCookie(
                JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                60 * 60 * 24 // 1일
        ));

        response.addCookie(jwtTokenProvider.createCookie(
                JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                60 * 60 * 24 * 7 // 7일
        ));

        log.info("✅ [OAuth2 로그인 성공] 토큰 발급 완료");

        // 프론트엔드 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}