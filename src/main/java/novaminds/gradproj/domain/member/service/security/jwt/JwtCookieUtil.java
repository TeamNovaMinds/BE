package novaminds.gradproj.domain.member.service.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class JwtCookieUtil {

    @Value("${app.cookie.secure:false}") //TODO: 배포하면 true로 설정
    private boolean secureCookie;

    @Value("${app.cookie.same-site:Lax}") //TODO: 배포하면 None으로 설정 가능
    private String sameSite;

    /**
     * 쿠키에 토큰 추가
     */
    public void addTokenToCookie(HttpServletResponse response, String cookieName, String token, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(maxAge)
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * 요청에서 특정 쿠키값 추출
     */
    public Optional<String> resolveToken(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * 쿠키 삭제
     */
    public void deleteTokenCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(0)
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}