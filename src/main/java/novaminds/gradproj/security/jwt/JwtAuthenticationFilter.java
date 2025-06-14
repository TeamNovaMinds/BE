package novaminds.gradproj.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.security.auth.CustomUserDetailsService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("📌 [JWT 필터] ================ 요청 시작: {} {} ================",
                request.getMethod(), request.getRequestURI());

        try {
            // 쿠키에서 토큰 추출
            String accessToken = extractTokenFromCookies(request, JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME);
            String refreshToken = extractTokenFromCookies(request, JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME);

            log.info("🔍 [JWT 필터] 액세스 토큰: {}", accessToken != null ? "존재함" : "없음");
            log.info("🔍 [JWT 필터] 리프레시 토큰: {}", refreshToken != null ? "존재함" : "없음");

            boolean authenticated = false;
            boolean isPermitAllUrl = isPermitAllUrl(request.getRequestURI());

            log.info("🔍 [JWT 필터] 현재 URL '{}' - 인증 필수 여부: {}",
                    request.getRequestURI(), !isPermitAllUrl ? "필수" : "불필요");

            // 액세스 토큰 처리
            if (StringUtils.hasText(accessToken)) {
                log.info("🔄 [JWT 필터] 액세스 토큰 검증 시작");
                try {
                    authenticated = processAccessToken(request, accessToken);
                    log.info("✅ [JWT 필터] 액세스 토큰 검증 결과: {}", authenticated ? "성공" : "실패");
                } catch (ExpiredJwtException e) {
                    log.warn("⏰ [JWT 필터] 액세스 토큰 만료됨 - 리프레시 토큰으로 재발급 시도");
                    deleteCookie(response, JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME);

                    if (StringUtils.hasText(refreshToken)) {
                        log.info("🔄 [JWT 필터] 리프레시 토큰으로 액세스 토큰 재발급 시도");
                        authenticated = refreshAccessToken(request, response, refreshToken);
                        log.info("🔄 [JWT 필터] 토큰 재발급 결과: {}", authenticated ? "성공" : "실패");

                        if (authenticated) {
                            log.info("✅ [JWT 필터] 토큰 재발급 성공 - 요청 계속 진행");
                            filterChain.doFilter(request, response);
                            return;
                        }
                    } else {
                        log.warn("❌ [JWT 필터] 액세스 토큰 만료 & 리프레시 토큰 없음");
                    }
                }
            } else if (StringUtils.hasText(refreshToken)) {
                // 액세스 토큰은 없지만 리프레시 토큰이 있는 경우
                log.info("🔄 [JWT 필터] 액세스 토큰 없음, 리프레시 토큰으로 액세스 토큰 발급 시도");
                authenticated = refreshAccessToken(request, response, refreshToken);
                log.info("🔄 [JWT 필터] 토큰 발급 결과: {}", authenticated ? "성공" : "실패");

                if (authenticated) {
                    log.info("✅ [JWT 필터] 토큰 발급 성공 - 요청 계속 진행");
                    filterChain.doFilter(request, response);
                    return;
                }
            } else {
                log.info("ℹ️ [JWT 필터] 액세스 토큰 & 리프레시 토큰 모두 없음");
            }

            // 인증 실패 & 회원 전용 URL인 경우 401 에러 반환
            if (!authenticated && !isPermitAllUrl) {
                log.warn("⛔ [JWT 필터] 인증 실패 & 인증 필수 URL - 접근 거부");
                handleAuthenticationError(response, "로그인이 필요한 서비스입니다.");
                return;
            }

            if (authenticated) {
                log.info("✅ [JWT 필터] 인증 성공 - 요청 계속 진행");
            } else if (isPermitAllUrl) {
                log.info("🔓 [JWT 필터] 인증 불필요 URL - 비회원으로 요청 계속 진행");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("⚠️ [JWT 필터] 예외 발생: {}", e.getMessage(), e);

            if (!isPermitAllUrl(request.getRequestURI())) {
                log.error("⛔ [JWT 필터] 인증 필수 URL에서 예외 발생 - 접근 거부");
                handleAuthenticationError(response, "인증 처리 중 오류가 발생했습니다.");
                return;
            }

            log.info("🔓 [JWT 필터] 인증 불필요 URL에서 예외 발생 - 비회원으로 요청 계속 진행");
            filterChain.doFilter(request, response);

        } finally {
            log.info("📌 [JWT 필터] ================ 요청 처리 완료: {} {} ================",
                    request.getMethod(), request.getRequestURI());
        }
    }

    // 액세스 토큰 검증 및 처리
    private boolean processAccessToken(HttpServletRequest request, String accessToken) {
        log.info("🔎 [JWT 필터] 액세스 토큰 검증 과정 시작");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            log.error("❌ [JWT 필터] 액세스 토큰 검증 실패 - 유효하지 않은 토큰");
            return false;
        }
        log.info("✓ [JWT 필터] 액세스 토큰 서명 유효성 검증 성공");

        String category = jwtTokenProvider.getCategory(accessToken);
        if (!"access".equals(category)) {
            log.error("❌ [JWT 필터] 액세스 토큰 종류 검증 실패 - 현재 종류: {}", category);
            return false;
        }
        log.info("✓ [JWT 필터] 액세스 토큰 종류 검증 성공");

        if (jwtTokenProvider.isExpired(accessToken)) {
            log.error("❌ [JWT 필터] 액세스 토큰 만료됨");
            throw new ExpiredJwtException(null, null, "토큰이 만료되었습니다");
        }
        log.info("✓ [JWT 필터] 액세스 토큰 만료 여부 검증 성공");

        String loginId = jwtTokenProvider.getLoginIdFromToken(accessToken);
        log.info("✓ [JWT 필터] 토큰에서 추출한 loginId: {}", loginId);

        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
            log.info("✓ [JWT 필터] DB에서 사용자 정보 조회 성공: {}", loginId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("✅ [JWT 필터] 인증 정보 저장 완료: 사용자 {}, 권한: {}", loginId, userDetails.getAuthorities());
            return true;
        } catch (Exception e) {
            log.error("❌ [JWT 필터] 사용자 정보 조회 또는 인증 설정 실패: {}", e.getMessage());
            return false;
        }
    }

    // 리프레시 토큰으로 액세스 토큰 재발급
    private boolean refreshAccessToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        log.info("🔄 [JWT 필터] 리프레시 토큰으로 액세스 토큰 재발급 과정 시작");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.error("❌ [JWT 필터] 리프레시 토큰 검증 실패 - 유효하지 않은 토큰");
            deleteCookie(response, JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME);
            return false;
        }
        log.info("✓ [JWT 필터] 리프레시 토큰 서명 유효성 검증 성공");

        String category = jwtTokenProvider.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            log.error("❌ [JWT 필터] 리프레시 토큰 종류 검증 실패 - 현재 종류: {}", category);
            deleteCookie(response, JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME);
            return false;
        }
        log.info("✓ [JWT 필터] 리프레시 토큰 종류 검증 성공");

        if (jwtTokenProvider.isExpired(refreshToken)) {
            log.error("❌ [JWT 필터] 리프레시 토큰 만료됨");
            deleteCookie(response, JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME);
            return false;
        }
        log.info("✓ [JWT 필터] 리프레시 토큰 만료 여부 검증 성공");

        try {
            String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
            log.info("✓ [JWT 필터] 리프레시 토큰에서 추출한 loginId: {}", loginId);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
            log.info("✓ [JWT 필터] DB에서 사용자 정보 조회 성공: {}", loginId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // 새 액세스 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            log.info("✓ [JWT 필터] 새 액세스 토큰 생성 성공: 사용자 {}", loginId);

            // ResponseCookie 사용으로 변경 (modified)
            ResponseCookie accessCookie = ResponseCookie.from(
                            JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME,
                            newAccessToken)
                    .path("/")
                    .httpOnly(true)
                    .secure(false) // 개발환경
                    .sameSite("Lax") // SameSite 추가 (added)
                    .maxAge(24 * 60 * 60) // 24시간
                    .build();

            // addHeader 방식으로 변경 (modified)
            response.addHeader("Set-Cookie", accessCookie.toString());
            log.info("✓ [JWT 필터] 새 액세스 토큰 쿠키 설정 완료");

            // 인증 정보 설정
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("✅ [JWT 필터] 인증 정보 저장 완료: 사용자 {}, 권한: {}", loginId, userDetails.getAuthorities());

            return true;
        } catch (Exception e) {
            log.error("❌ [JWT 필터] 액세스 토큰 재발급 과정 중 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // 인증 오류 처리
    private void handleAuthenticationError(HttpServletResponse response, String message) throws IOException {
        log.error("⛔ [JWT 필터] 인증 실패 처리: {}", message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.onFailure("AUTH401", message, null);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        log.info("✓ [JWT 필터] 401 응답 전송 완료: {}", message);
    }

    // 쿠키에서 토큰 추출
    private String extractTokenFromCookies(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("🍪 [JWT 필터] 요청에 쿠키가 없음");
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                log.info("🍪 [JWT 필터] 쿠키 '{}' 발견", cookieName);
                return cookie.getValue();
            }
        }

        log.info("🍪 [JWT 필터] 쿠키 '{}' 발견되지 않음", cookieName);
        return null;
    }

    // 쿠키 삭제
    private void deleteCookie(HttpServletResponse response, String cookieName) {
        // ResponseCookie 사용으로 변경 (modified)
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .path("/")
                .httpOnly(true)
                .secure(false) // 개발환경
                .sameSite("Lax") // ✅ SameSite 추가 (added)
                .maxAge(0)
                .build();

        // addHeader 방식으로 변경
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("🍪 [JWT 필터] 쿠키 삭제 완료: {}", cookieName);
    }

    // 허용된 URL 확인
    private boolean isPermitAllUrl(String requestURI) {
        boolean isPermit = requestURI.equals("/") ||
                (requestURI.startsWith("/auth/") &&
                        !requestURI.startsWith("/auth/additional-info")) ||
                requestURI.startsWith("/oauth2/") ||
                requestURI.startsWith("/login/oauth2/") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.equals("/favicon.ico");

        if (isPermit) {
            log.info("🔓 [JWT 필터] URL '{}': 인증 불필요 URL", requestURI);
        } else {
            log.info("🔒 [JWT 필터] URL '{}': 인증 필수 URL", requestURI);
        }

        return isPermit;
    }
}