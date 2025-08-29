package novaminds.gradproj.domain.member.service.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.warn("❌ [인가 실패] 접근 권한이 없는 요청입니다: {}", accessDeniedException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // ApiResponse를 사용하여 일관된 403 에러 응답을 생성합니다.
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(ErrorStatus._FORBIDDEN);

        // ObjectMapper를 사용하여 response body에 JSON을 작성합니다.
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
