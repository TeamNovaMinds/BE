package novaminds.gradproj.domain.member.service.security.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.service.security.jwt.JwtCookieService;
import novaminds.gradproj.domain.member.service.security.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieService jwtCookieService;

    public void generateAndSetTokens(Authentication authentication, HttpServletResponse response) {

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        jwtCookieService.setTokenCookies(accessToken, refreshToken, response);
    }
}