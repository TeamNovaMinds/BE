package novaminds.gradproj.domain.member.service.security.jwt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.config.properties.JwtProperties;
import novaminds.gradproj.domain.member.service.security.auth.AuthRedisService;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLoginProcessor {

    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthRedisService authRedisService;

    /**
     * 로그인 처리: JWT 토큰 생성, 쿠키 설정, Redis 저장
     */
    public void processLogin(HttpServletResponse response, Authentication authentication) {
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // 쿠키에 토큰 저장
        int accessTokenMaxAge = jwtProperties.getExpiration().intValue(); // 24시간
        int refreshTokenMaxAge = jwtProperties.getRefreshExpiration().intValue(); // 7일

        jwtCookieUtil.addTokenToCookie(response, "accessToken", accessToken, accessTokenMaxAge);
        jwtCookieUtil.addTokenToCookie(response, "refreshToken", refreshToken, refreshTokenMaxAge);

        // Redis에 Refresh Token 저장
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String loginId = principalDetails.getMember().getLoginId();

        authRedisService.saveRefreshToken(
                loginId,
                refreshToken,
                Duration.ofDays(7)
        );
    }

    /**
     * 로그아웃 처리: 토큰 블랙리스트 추가 및 쿠키 삭제
     */
    public void processLogout(HttpServletResponse response, String accessToken, String refreshToken) {
        // 액세스 토큰 블랙리스트 추가
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            long remainingTime = jwtTokenProvider.getRemainingTime(accessToken);
            if (remainingTime > 0) {
                authRedisService.addToBlacklist(accessToken, Duration.ofMillis(remainingTime));
            }
        }

        // 리프레시 토큰 처리
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
            authRedisService.deleteRefreshToken(loginId);

            // 리프레시 토큰도 블랙리스트 추가
            long remainingTime = jwtTokenProvider.getRemainingTime(refreshToken);
            if (remainingTime > 0) {
                authRedisService.addToBlacklist(refreshToken, Duration.ofMillis(remainingTime));
            }
        }

        // 쿠키 삭제
        jwtCookieUtil.deleteTokenCookie(response, "accessToken");
        jwtCookieUtil.deleteTokenCookie(response, "refreshToken");
    }
}
