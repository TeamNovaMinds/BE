package novaminds.gradproj.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.user.*;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
import novaminds.gradproj.security.auth.PrincipalDetails;
import novaminds.gradproj.security.jwt.JwtTokenProvider;
import novaminds.gradproj.web.dto.auth.AuthRequest;
import novaminds.gradproj.web.dto.auth.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final UserInterestCategoryRepository userInterestCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse.SignupResponse signup(AuthRequest.SignupRequest request, HttpServletResponse response) {
        log.info("🔄 [회원가입] 시작 - 이메일: {}", request.getEmail());

        // 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("❌ [회원가입] 이메일 중복 - {}", request.getEmail());
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // loginId 생성 (LOCAL_UUID앞8자리)
        String loginId = generateLoginId(SocialType.LOCAL, null);
        log.info("✓ [회원가입] loginId 생성 완료: {}", loginId);

        // 임시 닉네임 생성
        String tempNickname = "user_" + UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .loginId(loginId)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(tempNickname)
                .role(Role.USER)
                .socialType(SocialType.LOCAL)
                .isProfileCompleted(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("✅ [회원가입] 기본 정보 저장 완료 - loginId: {}, email: {}", savedUser.getLoginId(), savedUser.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(savedUser), null, new PrincipalDetails(savedUser).getAuthorities()
        );

        SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = contextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        contextHolderStrategy.setContext(context);

        //SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Cookie 설정
        setCookies(response, accessToken, refreshToken);
        log.info("✅ [회원가입] 토큰 발급 완료");

        return AuthResponse.SignupResponse.from(savedUser);
    }

    // 추가 정보 입력 (닉네임, 관심 카테고리)
    @Transactional
    public AuthResponse.AdditionalInfoResponse completeProfile(String loginId, AuthRequest.AdditionalInfoRequest request) {
        log.info("🔄 [추가 정보 입력] 시작 - loginId: {}", loginId);

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 닉네임 중복 확인 (현재 사용자의 닉네임과 다른 경우에만)
        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.findByNickname(request.getNickname()).isPresent()) {
            log.error("❌ [추가 정보 입력] 닉네임 중복 - {}", request.getNickname());
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 닉네임 업데이트
        user.updateNickname(request.getNickname());

        // 기존 관심 카테고리 삭제
        userInterestCategoryRepository.deleteByUserLoginId(loginId);

        // 새로운 관심 카테고리 저장
        List<RecipeCategory> categories = request.getInterestCategories();
        for (RecipeCategory category : categories) {
            UserInterestCategory interestCategory = UserInterestCategory.create(user, category);
            userInterestCategoryRepository.save(interestCategory);
        }

        // 프로필 완료 상태로 변경
        user.completeProfile();

        log.info("✅ [추가 정보 입력] 완료 - loginId: {}, 닉네임: {}, 관심 카테고리 수: {}",
                loginId, request.getNickname(), categories.size());

        return AuthResponse.AdditionalInfoResponse.from(user);
    }

    // 로그인
    public AuthResponse.LoginResponse login(AuthRequest.LoginRequest request, HttpServletResponse response) {
        log.info("🔄 [로그인] 시작 - 이메일: {}", request.getEmail());

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("❌ [로그인] 사용자 없음 - {}", request.getEmail());
                    return new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
                });

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLoginId(), request.getPassword())
        );
        log.info("✓ [로그인] 인증 성공 - loginId: {}", user.getLoginId());

        SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = contextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        contextHolderStrategy.setContext(context);

        //SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        log.info("✓ [로그인] 토큰 생성 완료");

        // Cookie 설정
        setCookies(response, accessToken, refreshToken);
        log.info("✅ [로그인] 완료 - loginId: {}, email: {}, 프로필 완료: {}",
                user.getLoginId(), user.getEmail(), user.isProfileCompleted());

        return AuthResponse.LoginResponse.from(user);
    }

    // 로그아웃
    public void logout(HttpServletResponse response) {
        log.info("🔄 [로그아웃] 시작");

        // Cookie 삭제
        Cookie accessTokenCookie = new Cookie(JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME, "");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);

        Cookie refreshTokenCookie = new Cookie(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        log.info("✅ [로그아웃] 완료 - 쿠키 삭제됨");
    }

    // 토큰 재발급
    public void refreshToken(String refreshToken, HttpServletResponse response) {
        log.info("🔄 [토큰 재발급] 시작");

        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.error("❌ [토큰 재발급] 리프레시 토큰 검증 실패");
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
        User user = userRepository.findById(loginId)
                .orElseThrow(() -> {
                    log.error("❌ [토큰 재발급] 사용자 없음 - loginId: {}", loginId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        // 새 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(user), null, new PrincipalDetails(user).getAuthorities()
        );

        SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = contextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        contextHolderStrategy.setContext(context);

        //SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authentication);

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Cookie 설정
        setCookies(response, newAccessToken, newRefreshToken);

        log.info("✅ [토큰 재발급] 완료 - loginId: {}", loginId);
    }

    @Transactional(readOnly = true)
    public AuthResponse.LoginResponse getProfile(User user) {
        return AuthResponse.LoginResponse.from(user);
    }

    // loginId 생성 헬퍼 메서드
    private String generateLoginId(SocialType socialType, String providerId) {
        switch (socialType) {
            case GOOGLE:
                return "GOOGLE_" + providerId;
            case NAVER:
                return "NAVER_" + providerId;
            case LOCAL:
                return "LOCAL_" + UUID.randomUUID().toString().substring(0, 8);
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다.");
        }
    }

    // Cookie 설정 헬퍼 메서드
    private void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessTokenCookie = jwtTokenProvider.createCookie(
                JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                60 * 60 * 24 // 1일
        );

        Cookie refreshTokenCookie = jwtTokenProvider.createCookie(
                JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                60 * 60 * 24 * 7 // 7일
        );

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}