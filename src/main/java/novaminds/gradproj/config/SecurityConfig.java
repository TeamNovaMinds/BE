package novaminds.gradproj.config;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.security.jwt.JwtAuthenticationFilter;
import novaminds.gradproj.security.oauth2.CustomOAuth2UserService;
import novaminds.gradproj.security.oauth2.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //기본 보안 설정 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 비활성화
                //CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //세션 설정 - STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 공개 접근 허용
                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()
                        // 관리자 전용
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                //OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 커스텀 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )
                //JWT 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 프론트엔드 URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Cookie 허용
        configuration.setMaxAge(3600L); // preflight 캐시 시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}