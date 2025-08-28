package novaminds.gradproj.domain.member.service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.config.properties.JwtProperties;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    // SecretKey 생성
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성
    public String generateAccessToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(principalDetails.getUsername()) // loginId
                .claim("role", principalDetails.getMember().getRole())
                .claim("profileCompleted", principalDetails.getMember().isProfileCompleted())
                .claim("category", "access")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(principalDetails.getUsername()) // loginId
                .claim("category", "refresh")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // 토큰에서 loginId 추출
    public String getLoginIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    // 토큰 카테고리 추출
    public String getCategory(String token) {
        Claims claims = getClaims(token);
        return claims.get("category", String.class);
    }

    // 토큰에서 role 추출
    public String getRoleFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    // JWT 액세스 토큰으로부터 PrincipalDetails 생성
    public PrincipalDetails createPrincipalFromAccessToken(String accessToken) {
        String loginId = getLoginIdFromToken(accessToken);
        String role = getRoleFromToken(accessToken);
        boolean profileCompleted = getClaims(accessToken).get("profileCompleted", Boolean.class);
        return PrincipalDetails.fromJwtClaims(loginId, role, profileCompleted);
    }

    // JWT 리프레시 토큰으로부터 PrincipalDetails 생성 (DB 조회 없이)
    public PrincipalDetails createPrincipalFromRefreshToken(String refreshToken) {
        String loginId = getLoginIdFromToken(refreshToken);

        // 리프레시 토큰에는 role과 profileCompleted 정보가 없으므로 기본값 사용
        return PrincipalDetails.fromJwtClaims(loginId, "ROLE_USER", false);
    }

    // 토큰 만료 확인
    public boolean isExpired(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            log.error("JWT 토큰 검증 실패: {}", ex.getMessage());
            return false;
        }
    }

    // 토큰에서 만료 시간 추출
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    // Claims 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰의 남은 유효 시간을 밀리초로 반환
     * 
     * @param token JWT 토큰 (액세스 또는 리프레시)
     * @return 남은 시간(밀리초), 이미 만료된 경우 음수 반환
     */
    public long getRemainingTime(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().getTime() - System.currentTimeMillis();
        } catch (Exception e) {
            log.warn("토큰 남은 시간 계산 실패: {}", e.getMessage());
            return 0; // 오류 시 0 반환 (만료로 간주)
        }
    }
}