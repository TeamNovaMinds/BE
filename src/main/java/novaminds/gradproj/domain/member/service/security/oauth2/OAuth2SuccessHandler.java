package novaminds.gradproj.domain.member.service.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import novaminds.gradproj.domain.member.service.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseCookie;
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

        ResponseCookie accessCookie = jwtTokenProvider.createResponseCookie(
                JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                60 * 60 * 24 // 1일
        );

        ResponseCookie refreshCookie = jwtTokenProvider.createResponseCookie(
                JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                60 * 60 * 24 * 7 // 7일
        );

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        log.info("✅ [OAuth2 로그인 성공] 토큰 발급 완료");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        boolean isProfileCompleted = principalDetails.getMember().isProfileCompleted();

        if (!isProfileCompleted) {
            log.info("🔄 [OAuth2 로그인] 추가 정보 입력 페이지로 리다이렉트");
            getRedirectStrategy().sendRedirect(request, response, "/auth/additional-info");
        } else {
            log.info("🔄 [OAuth2 로그인] 메인 페이지로 리다이렉트");
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}