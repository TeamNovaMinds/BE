package novaminds.gradproj.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.user.UserRepository;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        // @CurrentUser User 타입 지원
        boolean isCurrentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class) != null;
        boolean isUserClass = User.class.equals(parameter.getParameterType());

        // @CurrentLoginId String 타입 지원
        boolean isCurrentLoginIdAnnotation = parameter.getParameterAnnotation(CurrentLoginId.class) != null;
        boolean isStringClass = String.class.equals(parameter.getParameterType());

        return (isCurrentUserAnnotation && isUserClass) || (isCurrentLoginIdAnnotation && isStringClass);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("⚠️ [ArgumentResolver] 인증되지 않은 사용자");
            return null;
        }

        // PrincipalDetails에서 loginId 추출
        String loginId = null;
        if (authentication.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            loginId = principalDetails.getUsername();
        }

        if (loginId == null) {
            log.warn("⚠️ [ArgumentResolver] loginId를 찾을 수 없음");
            return null;
        }

        // @CurrentLoginId인 경우 loginId 반환
        if (parameter.getParameterAnnotation(CurrentLoginId.class) != null) {
            log.info("✅ [ArgumentResolver] loginId 주입: {}", loginId);
            return loginId;
        }

        // @CurrentUser인 경우 User 객체 반환
        if (parameter.getParameterAnnotation(CurrentUser.class) != null) {
            User user = userRepository.findById(loginId)
                    .orElse(null);

            if (user != null) {
                log.info("✅ [ArgumentResolver] User 객체 주입: {}", user.getLoginId());
            } else {
                log.warn("⚠️ [ArgumentResolver] 사용자를 찾을 수 없음: {}", loginId);
            }

            return user;
        }

        return null;
    }
}
