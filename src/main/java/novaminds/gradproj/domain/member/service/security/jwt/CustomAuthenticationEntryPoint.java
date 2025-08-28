package novaminds.gradproj.domain.member.service.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("❌ [인증 실패] 자격 증명이 없는 요청입니다: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // ApiResponse를 사용하여 일관된 에러 응답을 생성합니다.
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                ErrorStatus._UNAUTHORIZED.getCode(),
                ErrorStatus._UNAUTHORIZED.getMessage(),
                null
        );

        // ObjectMapper를 사용하여 response body에 JSON을 작성합니다.
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
