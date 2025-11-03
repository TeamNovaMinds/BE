package novaminds.gradproj.domain.member.service.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * <p>
 * 인증과 관련된 Redis 작업을 처리하는 서비스:
 * <ul>
 * <li>Refresh Token 관리 (저장, 조회, 삭제)</li>
 * <li>Token Blacklist 관리 (블랙리스트 추가, 확인)</li>
 * <li>Password Reset Token 관리 (저장, 조회, 삭제)</li>
 * </ul>
 * 
 * <p>
 * 스프링 캐시를 활용하여 자주 조회되는 데이터의 성능을 최적화
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // Redis 키 접두사 상수 정의 (RedisConfig와 일치)
    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String PASSWORD_RESET_PREFIX = "passwordReset:";
    private static final String PASSWORD_RESET_TOKEN_PREFIX = "passwordResetToken:";

    /**
     * Refresh Token을 Redis에 저장
     *
     * @param loginId      사용자의 로그인 ID
     * @param refreshToken 저장할 리프레시 토큰
     * @param expiration   토큰의 만료 시간
     */
    @CacheEvict(value = "refreshToken", key = "#loginId")
    public void saveRefreshToken(String loginId, String refreshToken, Duration expiration) {
        // Redis 키 생성
        String key = REFRESH_TOKEN_PREFIX + loginId;
        
        // 토큰을 만료 시간과 함께 Redis에 저장
        redisTemplate.opsForValue().set(key, refreshToken, expiration);
    }

    /**
     * 사용자의 Refresh Token을 Redis에서 조회
     *
     * @param loginId 사용자의 로그인 ID
     * @return        저장된 리프레시 토큰, 없으면 null
     */
    @Cacheable(value = "refreshToken", key = "#loginId")
    public String getRefreshToken(String loginId) {
        // Redis 키 생성
        String key = REFRESH_TOKEN_PREFIX + loginId;
        
        // Redis에서 토큰 조회
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 사용자의 Refresh Token을 Redis에서 삭제 (로그아웃 시 사용)
     *
     * @param loginId 사용자의 로그인 ID
     */
    @CacheEvict(value = "refreshToken", key = "#loginId")
    public void deleteRefreshToken(String loginId) {
        // Redis 키 생성
        String key = REFRESH_TOKEN_PREFIX + loginId;
        
        // Redis에서 토큰 삭제
        redisTemplate.delete(key);
    }

    /**
     * 토큰을 블랙리스트에 추가 (로그아웃된 토큰의 재사용 방지)
     *
     * @param token 블랙리스트에 추가할 토큰
     * @param ttl   블랙리스트 유지 시간 (토큰 만료시간과 동일하게 설정)
     */
    public void addToBlacklist(String token, Duration ttl) {
        // Redis 키 생성
        String key = BLACKLIST_PREFIX + token;
        
        // 토큰을 블랙리스트에 TTL과 함께 저장
        redisTemplate.opsForValue().set(key, "blacklisted", ttl);
    }

    /**
     * 토큰이 블랙리스트에 등록되어 있는지 확인
     *
     * @param token 확인할 토큰
     * @return      블랙리스트에 등록되어 있으면 true, 그렇지 않으면 false
     */
    public boolean isBlacklisted(String token) {
        // Redis 키 생성
        String key = BLACKLIST_PREFIX + token;
        
        // Redis에서 키 존재 여부 확인
        return redisTemplate.hasKey(key);
    }

    /**
     * 비밀번호 재설정 토큰을 Redis에 저장 (양방향 매핑)
     * email -> token, token -> email 모두 저장하여 양방향 조회 가능
     *
     * @param email      사용자의 이메일 주소
     * @param token      비밀번호 재설정 토큰 (6자리 숫자)
     * @param expiration 토큰의 만료 시간
     */
    public void savePasswordResetToken(String email, String token, Duration expiration) {
        // 이메일 -> 토큰 매핑
        String emailKey = PASSWORD_RESET_PREFIX + email;
        redisTemplate.opsForValue().set(emailKey, token, expiration);

        // 토큰 -> 이메일 역방향 매핑 (토큰으로 이메일 조회 가능)
        String tokenKey = PASSWORD_RESET_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(tokenKey, email, expiration);
    }

    /**
     * 비밀번호 재설정 토큰을 Redis에서 조회
     *
     * @param email 사용자의 이메일 주소
     * @return      저장된 재설정 토큰, 없으면 null
     */
    public String getPasswordResetToken(String email) {
        // Redis 키 생성
        String key = PASSWORD_RESET_PREFIX + email;
        
        // Redis에서 토큰 조회
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 토큰으로 이메일 주소를 Redis에서 조회 (역방향 조회)
     *
     * @param token 비밀번호 재설정 토큰
     * @return      해당 토큰과 연결된 이메일 주소, 없으면 null
     */
    public String getEmailByPasswordResetToken(String token) {
        // Redis 키 생성
        String key = PASSWORD_RESET_TOKEN_PREFIX + token;

        // Redis에서 이메일 조회
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 비밀번호 재설정 토큰을 Redis에서 삭제 (재설정 완료 후 사용)
     * 양방향 매핑 모두 삭제
     *
     * @param email 사용자의 이메일 주소
     */
    public void deletePasswordResetToken(String email) {
        // 이메일 -> 토큰 매핑 조회
        String token = getPasswordResetToken(email);

        // 이메일 -> 토큰 매핑 삭제
        String emailKey = PASSWORD_RESET_PREFIX + email;
        redisTemplate.delete(emailKey);

        // 토큰 -> 이메일 역방향 매핑 삭제
        if (token != null) {
            String tokenKey = PASSWORD_RESET_TOKEN_PREFIX + token;
            redisTemplate.delete(tokenKey);
        }
    }
}