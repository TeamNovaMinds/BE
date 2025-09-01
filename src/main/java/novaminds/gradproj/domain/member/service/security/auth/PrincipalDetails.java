package novaminds.gradproj.domain.member.service.security.auth;

import lombok.Getter;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    // 일반 로그인용 생성자
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // OAuth2 로그인용 생성자
    public PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    /**
     * JWT Claims로부터 PrincipalDetails 생성 (DB 조회 없이)
     * JWT 인증 필터에서 사용하여 성능을 최적화
     */
    public static PrincipalDetails fromJwtClaims(String loginId, String roleString, boolean profileCompleted) {
        Role role = Role.valueOf(roleString);
        
        // JWT 검증 목적으로만 사용하는 최소한의 Member 객체 생성
        Member jwtMember = Member.builder()
                .loginId(loginId)
                .role(role)
                .profileCompleted(profileCompleted)
                .build();
                
        return new PrincipalDetails(jwtMember);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return member.getLoginId();
    }
}