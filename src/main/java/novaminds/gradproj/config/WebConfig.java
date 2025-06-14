package novaminds.gradproj.config;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.security.auth.CurrentUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000") // ✅ 패턴 추가 (added)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // ✅ OPTIONS, PATCH 추가 (added)
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // ✅ 쿠키 허용 (매우 중요!)
                .maxAge(3600); // ✅ preflight 캐시 시간 추가 (added)
    }
}
