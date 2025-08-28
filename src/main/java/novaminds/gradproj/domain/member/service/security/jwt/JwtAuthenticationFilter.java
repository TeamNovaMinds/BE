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
import novaminds.gradproj.domain.member.service.security.auth.AuthRedisService;
import novaminds.gradproj.domain.member.service.security.auth.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieService jwtCookieService;
    private final AuthRedisService authRedisService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 인증이 필요없는 URL 패턴 정의
    private static final List<String> PERMIT_ALL_PATTERNS = List.of(
            "/",
            "/auth/login",
            "/auth/signup", 
            "/auth/check-email",
            "/auth/send-reset-email",
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
                                    @Nonnull FilterChain filterChain
    ) throws IOException {

        String requestURI = request.getRequestURI();
        log.debug("🔍 [JWT 필터] 요청 처리 시작: {} {}", request.getMethod(), requestURI);

        try {
            // JWT 토큰 기반 인증 처리
            boolean authenticated = processAuthentication(request, response);

            if (authenticated) {
                log.debug("✅ [JWT 필터] 인증 성공 - 요청 계속 처리: {}", requestURI);
                filterChain.doFilter(request, response);
            } else {
                log.warn("⛔ [JWT 필터] 인증 실패 - 접근 거부: {}", requestURI);
                sendAuthenticationError(response, "로그인이 필요한 서비스입니다.");
            }

        } catch (Exception e) {
            log.error("❌ [JWT 필터] 인증 처리 중 예외 발생: {}", e.getMessage(), e);
            sendAuthenticationError(response, "인증 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * JWT 토큰 기반 인증 처리
     * 액세스 토큰 우선 검증, 만료 시 리프레시 토큰으로 재발급 시도
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @return 인증 성공 여부
     */
    private boolean processAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        // 쿠키에서 토큰 추출
        String accessToken = jwtCookieService.getTokenFromCookies(request, JwtCookieService.ACCESS_TOKEN_COOKIE_NAME)
                .orElse(null);
        String refreshToken = jwtCookieService.getTokenFromCookies(request, JwtCookieService.REFRESH_TOKEN_COOKIE_NAME)
                .orElse(null);

        // 액세스 토큰이 있는 경우 우선 검증
        if (StringUtils.hasText(accessToken)) {
            // 블랙리스트 확인
            if (authRedisService.isBlacklisted(accessToken)) {
                log.warn("🚫 [JWT 필터] 블랙리스트에 등록된 토큰");
                jwtCookieService.removeTokenCookies(response);
                return false;
            }

            try {
                return validateTokenAndSetAuthentication(accessToken, request);
            } catch (ExpiredJwtException e) {
                log.debug("⏰ [JWT 필터] 액세스 토큰 만료 - 리프레시 토큰으로 재발급 시도");
                // 만료된 액세스 토큰 쿠키 제거
                jwtCookieService.removeTokenCookies(response);
            }
        }

        // 리프레시 토큰으로 새 액세스 토큰 발급 시도
        if (StringUtils.hasText(refreshToken)) {
            return refreshAccessToken(refreshToken, request, response);
        }

        log.debug("ℹ️ [JWT 필터] 유효한 토큰 없음");
        return false;
    }

    /**
     * 토큰 유효성 검증 및 인증 정보 설정
     *
     * @param token   검증할 JWT 토큰
     * @param request HTTP 요청
     * @return 검증 성공 여부
     */
    private boolean validateTokenAndSetAuthentication(String token, HttpServletRequest request) {
        
        // 토큰 기본 유효성 검증
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("❌ [JWT 필터] 토큰 서명 검증 실패");
            return false;
        }

        // 토큰 만료 확인
        if (jwtTokenProvider.isExpired(token)) {
            throw new ExpiredJwtException(null, null, "토큰이 만료되었습니다");
        }

        // 사용자 정보 조회 및 인증 설정
        try {
            String loginId = jwtTokenProvider.getLoginIdFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

            // Spring Security 컨텍스트에 인증 정보 설정
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("✅ [JWT 필터] 인증 정보 설정 완료: {}", loginId);
            return true;

        } catch (Exception e) {
            log.error("❌ [JWT 필터] 사용자 정보 조회 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 리프레시 토큰으로 새 액세스 토큰 발급
     *
     * @param refreshToken 리프레시 토큰
     * @param request      HTTP 요청
     * @param response     HTTP 응답
     * @return 재발급 성공 여부
     */
    private boolean refreshAccessToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        
        try {
            // 리프레시 토큰 검증
            if (!jwtTokenProvider.validateToken(refreshToken) || jwtTokenProvider.isExpired(refreshToken)) {
                log.warn("❌ [JWT 필터] 리프레시 토큰 검증 실패");
                jwtCookieService.removeTokenCookies(response);
                return false;
            }

            // 토큰 카테고리 확인
            String category = jwtTokenProvider.getCategory(refreshToken);
            if (!"refresh".equals(category)) {
                log.warn("❌ [JWT 필터] 잘못된 토큰 타입: {}", category);
                jwtCookieService.removeTokenCookies(response);
                return false;
            }

            // 블랙리스트 확인
            if (authRedisService.isBlacklisted(refreshToken)) {
                log.warn("🚫 [JWT 필터] 블랙리스트에 등록된 리프레시 토큰");
                jwtCookieService.removeTokenCookies(response);
                return false;
            }

            // 사용자 정보 조회 및 새 액세스 토큰 발급
            String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

            // 새 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 새 액세스 토큰 생성 및 쿠키 설정
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            jwtCookieService.setTokenCookies(newAccessToken, refreshToken, response);

            // 인증 정보 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("✅ [JWT 필터] 토큰 재발급 성공: {}", loginId);
            return true;

        } catch (Exception e) {
            log.error("❌ [JWT 필터] 토큰 재발급 실패: {}", e.getMessage());
            jwtCookieService.removeTokenCookies(response);
            return false;
        }
    }

    /**
     * 인증 실패 시 401 에러 응답 전송
     *
     * @param response HTTP 응답
     * @param message  에러 메시지
     */
    private void sendAuthenticationError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.onFailure("AUTH401", message, null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}