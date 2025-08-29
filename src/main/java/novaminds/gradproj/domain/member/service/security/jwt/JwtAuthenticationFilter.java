package novaminds.gradproj.domain.member.service.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.service.security.auth.AuthRedisService;
import novaminds.gradproj.domain.member.service.security.auth.AuthenticationHelper;
import novaminds.gradproj.domain.member.service.security.auth.CustomUserDetailsService;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

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

    private final JwtLoginProcessor jwtLoginProcessor;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthRedisService authRedisService;
    private final AuthenticationHelper authenticationHelper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException {

        try {
            // 액세스 토큰 검증 및 처리
            if (!processAccessToken(request, response)) {
                // 액세스 토큰이 없거나 만료된 경우 리프레시 토큰으로 재발급 시도
                processRefreshToken(request, response);
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {
            // AuthenticationException은 Spring Security가 처리하도록 그대로 전달
            // CustomAuthenticationEntryPoint로 전달되어 처리된다.
            throw e;
        } catch (JwtException e) {
            // JwtTokenProvider에서 발생한 예외를 AuthenticationException으로 변환하여 던진다.
            throw new BadCredentialsException("유효하지 않은 JWT 토큰입니다.", e);
        } catch (Exception e) {
            log.error("❌ [JWT 필터] 알 수 없는 예외 발생: {}", e.getMessage(), e);
            // 그 외 예외는 일반적인 서버 에러로 처리될 수 있도록 던진다.
            throw new ServletException(e.getMessage(), e);
        }
    }

    /**
     * 액세스 토큰 처리
     * @return 유효한 토큰인 경우 true, 그렇지 않으면 false
     */
    private boolean processAccessToken(HttpServletRequest request, HttpServletResponse response) {
        var opt = jwtCookieUtil.resolveToken(request, "accessToken").filter(StringUtils::hasText);
        if (opt.isEmpty()) {
            return false;
        }

        String token = opt.get();

        // 1. 블랙리스트 확인 -> 블랙리스트에 있으면 쿠키를 삭제하고 실패 처리
        if (authRedisService.isBlacklisted(token)) {
            jwtCookieUtil.deleteTokenCookie(response, "accessToken");
            return false;
        }

        // 2. 토큰 카테고리가 'access'인지 확인 -> 아니면 쿠키를 삭제하고 실패 처리
        if (!"access".equals(jwtTokenProvider.getCategory(token))) {
            jwtCookieUtil.deleteTokenCookie(response, "accessToken");
            return false;
        }

        try {
            // 3. 토큰 유효성 검증 (만료 시 ExpiredJwtException 발생)
            jwtTokenProvider.validateToken(token);
            setAuthentication(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 4. 만료 예외 처리: 쿠키를 삭제하고 false를 반환하여 리프레시 토큰 처리 단계로 넘어감
            jwtCookieUtil.deleteTokenCookie(response, "accessToken");
            return false;
        }
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     * 이때 refresh token 자체로 access token을 발급 받는 것이 아니라,
     * access token을 재발급 받을 자격이 있는지 확인하는 것 뿐이다.
     */
    private void processRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        jwtCookieUtil.resolveToken(request, "refreshToken")
                .filter(StringUtils::hasText)
                // 1. 토큰의 유효성 먼저 검증
                .filter(jwtTokenProvider::validateToken)
                // 2. refresh 카테고리만 허용
                .filter(token -> "refresh".equals(jwtTokenProvider.getCategory(token)))
                // 3. 블랙리스트에 없는 토큰만 통과
                .filter(token -> !authRedisService.isBlacklisted(token))
                .ifPresent(refreshToken -> {
                    try {
                        String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
                        String storedToken = authRedisService.getRefreshToken(loginId);

                        if (refreshToken.equals(storedToken)) {

                            // 4. 데이터베이스에서 최신 사용자 정보를 가져오기
                            PrincipalDetails principalDetails = (PrincipalDetails) customUserDetailsService.loadUserByUsername(loginId);
                            Authentication authentication = authenticationHelper.createAuthentication(principalDetails);

                            // 5. 최신 정보로 인증 정보 설정
                            authenticationHelper.setAuthentication(principalDetails);

                            // 6. 액세스 토큰과 리프레쉬 토큰 재발급
                            jwtLoginProcessor.issueAndSetTokens(response, authentication);
                        } else {
                            // 저장된 refresh token과 불일치 할 경우 토큰 삭제
                            jwtCookieUtil.deleteTokenCookie(response, "refreshToken");
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
}