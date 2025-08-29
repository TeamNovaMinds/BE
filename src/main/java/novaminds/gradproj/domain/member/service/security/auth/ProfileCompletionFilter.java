package novaminds.gradproj.domain.member.service.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ProfileCompletionFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 프로필 미완성 상태에서도 허용되는 경로
    private final List<String> excludedPaths = List.of(
            "/api/auth/logout",
            "/api/auth/additional-info-part1",
            "/api/auth/additional-info-part2",
            "/api/auth/check-email",
            "/api/s3/image/upload-url",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
        return excludedPaths.stream()
                .anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof PrincipalDetails principalDetails) {

            // 프로필 미완성 사용자 체크
            if (!principalDetails.getMember().isProfileCompleted()) {
                log.warn("⚠️ [프로필 체크] 미완성 프로필 사용자 접근 차단 - loginId: {}, URI: {}",
                        principalDetails.getMember().getLoginId(), request.getRequestURI());
                sendProfileIncompleteError(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendProfileIncompleteError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(ErrorStatus.PROFILE_NOT_COMPLETED);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
