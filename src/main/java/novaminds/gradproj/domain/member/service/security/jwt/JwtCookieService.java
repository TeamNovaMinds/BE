package novaminds.gradproj.domain.member.service.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class JwtCookieService {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private static final int ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 24; // 1일
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 60 * 60 * 24 * 7; // 7일

    public void setTokenCookies(String accessToken, String refreshToken, HttpServletResponse response) {
        ResponseCookie accessCookie = createResponseCookie(
                ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                ACCESS_TOKEN_EXPIRE_TIME
        );

        ResponseCookie refreshCookie = createResponseCookie(
                REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME
        );

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public Optional<String> getTokenFromCookies(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> tokenName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public void removeTokenCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = createResponseCookie(ACCESS_TOKEN_COOKIE_NAME, "", 0);
        ResponseCookie refreshCookie = createResponseCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0);

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 개발 환경에서는 false, 나중에 https 적용하고나서는 true
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    private ResponseCookie createResponseCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // 개발 환경에서는 false
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax") // CORS를 위한 필수 설정!
                .domain("localhost")
                .build();
    }
}