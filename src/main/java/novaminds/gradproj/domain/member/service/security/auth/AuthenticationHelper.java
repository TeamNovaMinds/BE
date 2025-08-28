package novaminds.gradproj.domain.member.service.security.auth;

import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Authentication 객체 생성 및 SecurityContext 설정을 담당하는 클래스
 * <p>
 * Spring Security의 Authentication 객체 생성과 SecurityContextHolder 설정 로직을 담당해서 코드 중복 줄여주는 클래스
 */
@Slf4j
@Service
public class AuthenticationHelper {

    /**
     * PrincipalDetails로부터 인증 정보를 생성하고 SecurityContext에 설정
     * 
     * @param principalDetails 인증할 사용자의 PrincipalDetails 객체
     */
    public void setAuthentication(PrincipalDetails principalDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, 
                null, 
                principalDetails.getAuthorities()
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Member 엔티티로부터 인증 정보를 생성하고 SecurityContext에 설정
     * 
     * @param member 인증할 사용자의 Member 엔티티
     */
    public void setAuthentication(Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        setAuthentication(principalDetails);
    }

    /**
     * PrincipalDetails로부터 Authentication 객체만 생성 (SecurityContext 설정하지 않음)
     * 토큰 생성 등의 목적으로 Authentication 객체가 필요한 경우 사용
     * 
     * @param principalDetails 사용자의 PrincipalDetails 객체
     * @return Authentication 객체
     */
    public Authentication createAuthentication(PrincipalDetails principalDetails) {
        return new UsernamePasswordAuthenticationToken(
                principalDetails, 
                null, 
                principalDetails.getAuthorities()
        );
    }

    /**
     * Member 엔티티로부터 Authentication 객체만 생성 (SecurityContext 설정하지 않음)
     * 
     * @param member 사용자의 Member 엔티티
     * @return Authentication 객체
     */
    public Authentication createAuthentication(Member member) {
        PrincipalDetails principalDetails = new PrincipalDetails(member);
        return createAuthentication(principalDetails);
    }
}