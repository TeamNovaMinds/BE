package novaminds.gradproj.domain.member.service.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.config.properties.JwtProperties;
import novaminds.gradproj.domain.member.service.security.auth.AuthRedisService;
import novaminds.gradproj.domain.member.service.security.auth.AuthenticationHelper;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 기반 인증 필터
 * <p>
 * HTTP 요청마다 실행되어 JWT 토큰을 검증하고 인증 처리를 수행:
 * <ul>
 * <li>쿠키에서 액세스/리프레시 토큰 추출</li>
 * <li>토큰 유효성 검증 및 블랙리스트 확인</li>
 * <li>토큰 만료 시 자동 재발급 처리</li>
 * <li>Spring Security 컨텍스트에 인증 정보 설정</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthRedisService authRedisService;
    private final AuthenticationHelper authenticationHelper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 인증이 필요없는 URL 패턴
    private static final List<String> PERMIT_ALL_PATTERNS = List.of(
            "/",
            "/auth/login",
            "/auth/signup",
            "/auth/check-email",
            "/auth/reset-password",
            "/oauth2/**",
            "/login/oauth2/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/favicon.ico"
    );

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return PERMIT_ALL_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws IOException {

        try {
            // 액세스 토큰 검증 및 처리
            if (!processAccessToken(request, response)) {
                // 액세스 토큰이 없거나 만료된 경우 리프레시 토큰으로 재발급 시도
                processRefreshToken(request, response);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("❌ [JWT 필터] 인증 처리 중 예외 발생: {}", e.getMessage(), e);
            sendAuthenticationError(response, "인증 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 액세스 토큰 처리
     * @return 유효한 토큰인 경우 true, 그렇지 않으면 false
     */
    private boolean processAccessToken(HttpServletRequest request, HttpServletResponse response) {
        return jwtCookieUtil.resolveToken(request, "accessToken")
                .filter(StringUtils::hasText)
                .map(accessToken -> {
                    // 블랙리스트 확인
                    if (authRedisService.isBlacklisted(accessToken)) {
                        jwtCookieUtil.deleteTokenCookie(response, "accessToken");
                        return false;
                    }

                    try {
                        // 토큰 검증 및 인증 설정
                        if (jwtTokenProvider.validateToken(accessToken)) {
                            setAuthentication(accessToken);
                            return true;
                        }
                    } catch (ExpiredJwtException e) {
                        jwtCookieUtil.deleteTokenCookie(response, "accessToken");
                    }
                    return false;
                })
                .orElse(false);
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     */
    private void processRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        jwtCookieUtil.resolveToken(request, "refreshToken")
                .filter(StringUtils::hasText)
                .filter(jwtTokenProvider::validateToken)
                .ifPresent(refreshToken -> {
                    try {
                        String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
                        String storedToken = authRedisService.getRefreshToken(loginId);

                        if (refreshToken.equals(storedToken)) {
                            // 인증 정보 설정
                            setAuthentication(refreshToken);
                            
                            // 새로운 액세스 토큰 생성을 위한 Authentication 객체 생성
                            PrincipalDetails principalDetails = jwtTokenProvider.createPrincipalFromToken(refreshToken);
                            Authentication authentication = authenticationHelper.createAuthentication(principalDetails);

                            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

                            jwtCookieUtil.addTokenToCookie(response, "accessToken", newAccessToken, jwtProperties.getExpiration().intValue());
                        }
                    } catch (Exception e) {
                        log.error("❌ [JWT 필터] 토큰 재발급 실패: {}", e.getMessage());
                        jwtCookieUtil.deleteTokenCookie(response, "refreshToken");
                    }
                });
    }

    /**
     * 인증 정보 설정 (JWT Claims로부터 직접 생성)
     */
    private void setAuthentication(String token) {
        PrincipalDetails principalDetails = jwtTokenProvider.createPrincipalFromToken(token);
        authenticationHelper.setAuthentication(principalDetails);
    }

    /**
     * 인증 실패 응답
     */
    private void sendAuthenticationError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.onFailure("AUTH401", message, null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}