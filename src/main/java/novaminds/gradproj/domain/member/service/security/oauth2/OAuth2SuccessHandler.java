package novaminds.gradproj.domain.member.service.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.AuthTokenService;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenService authTokenService;

    @Value("${app.oauth2.redirect.base-uri}")
    private String baseUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        authTokenService.generateAndSetTokens(authentication, response);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        String path = member.isProfileCompleted() ? "home" : "profile";

        // 기본 리다이렉트 URI
        String targetUrl = UriComponentsBuilder.fromUriString(baseUri)
                .pathSegment(path)
                .build().toUriString();

        // 성공 후 리다이렉트 URL 설정
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}