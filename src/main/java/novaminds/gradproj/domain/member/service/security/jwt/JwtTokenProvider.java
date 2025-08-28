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

}