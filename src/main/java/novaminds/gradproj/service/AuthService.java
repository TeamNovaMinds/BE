package novaminds.gradproj.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.handler.RefrigeratorSkinHandler;
import novaminds.gradproj.domain.refrigerator.Refrigerator;
import novaminds.gradproj.domain.refrigerator.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.user.*;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
import novaminds.gradproj.security.auth.PrincipalDetails;
import novaminds.gradproj.security.jwt.JwtTokenProvider;
import novaminds.gradproj.web.dto.auth.AuthRequest;
import novaminds.gradproj.web.dto.auth.AuthResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final EmailService emailService;
    private final S3Service s3Service;

    private final UserRepository userRepository;
    private final UserInterestCategoryRepository userInterestCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final UserRefrigeratorSkinRepository userRefrigeratorSkinRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
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

        createRefrigeratorForUser(savedUser);

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

    private void createRefrigeratorForUser(User user) {
        // 냉장고 생성
        Refrigerator refrigerator = Refrigerator.builder()
                .user(user)
                .build();

        refrigeratorRepository.save(refrigerator);
        user.setRefrigerator(refrigerator);

        // 기본 스킨 찾기
        RefrigeratorSkin defaultSkin = refrigeratorSkinRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new RefrigeratorSkinHandler(ErrorStatus.DEFAULT_REFRIGERATOR_SKIN_NOT_FOUND));

        // 기본 스킨을 유저에게 부여하고 장착
        UserRefrigeratorSkin userSkin = UserRefrigeratorSkin.builder()
                .user(user)
                .skin(defaultSkin)
                .isEquipped(true)
                .build();

        userRefrigeratorSkinRepository.save(userSkin);

        log.info("✅ [회원가입] 냉장고 및 기본 스킨 생성 완료 - userId: {}", user.getLoginId());
    }

    // 추가 정보 입력 (닉네임, 프로필 이미지)
    @Transactional
    public AuthResponse.AdditionalInfoResponse completeProfilePart1(
            User user,
            AuthRequest.AdditionalInfoNicknameRequest request,
            MultipartFile profileImage
    ) {

        // 닉네임 중복 확인 (현재 사용자의 닉네임과 다른 경우에만)
        if (!user.getNickname().equals(request.getNickname()) &&
                userRepository.findByNickname(request.getNickname()).isPresent()) {
            log.error("❌ [추가 정보 입력] 닉네임 중복 - {}", request.getNickname());
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 닉네임 업데이트
        user.updateNickname(request.getNickname());

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                if (user.getProfileImage() != null && user.getProfileImage().contains("amazonaws.com")) {
                    s3Service.deleteFile(user.getProfileImage());
                }

                String profileImgUrl = s3Service.uploadFile(profileImage, "profile");
                user.updateProfileImage(profileImgUrl);
                log.info("✓ [추가 정보 입력] 프로필 이미지 업로드 완료 - URL: {}", profileImgUrl);
            } catch (Exception e) {
                log.error("❌ [추가 정보 입력] 프로필 이미지 업로드 실패", e);
                throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.", e);
            }
        }

        log.info("✅ [추가 정보 입력] 완료 - loginId: {}, 닉네임: {}, 프로필 이미지: {}",
                user.getLoginId(), request.getNickname(), user.getProfileImage() != null ? "있음" : "없음");

        return AuthResponse.AdditionalInfoResponse.from(user);
    }

    // 추가 정보 입력 (닉네임, 프로필 이미지)
    @Transactional
    public AuthResponse.AdditionalInfoResponse completeProfilePart2(
            User user,
            AuthRequest.AdditionalInfoInterestRequest request
    ) {
        // 기존 관심 카테고리 삭제
        userInterestCategoryRepository.deleteByUserLoginId(user.getLoginId());

        // 새로운 관심 카테고리 저장
        List<RecipeCategory> categories = request.getInterestCategories();
        for (RecipeCategory category : categories) {
            UserInterestCategory interestCategory = UserInterestCategory.create(user, category);
            userInterestCategoryRepository.save(interestCategory);
        }

        // 프로필 완료 상태로 변경
        user.completeProfile();

        log.info("✅ [추가 정보 입력] 완료 - loginId: {}, 관심 카테고리 수: {}",
                user.getLoginId(), categories.size());

        return AuthResponse.AdditionalInfoResponse.from(user);
    }

    // 로그인
    @Transactional
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
    @Transactional
    public void logout(HttpServletResponse response) {
        log.info("🔄 [로그아웃] 시작");

        // ✅ ResponseCookie 사용으로 변경 (modified)
        ResponseCookie accessTokenCookie = ResponseCookie.from(JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax") // ✅ SameSite 추가 (added)
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax") // ✅ SameSite 추가 (added)
                .maxAge(0)
                .build();

        // ✅ addHeader 방식으로 변경 (modified)
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        log.info("✅ [로그아웃] 완료 - 쿠키 삭제됨");
    }

    public String checkEmailDuplication(String email) {
        log.info("🔄 [이메일 중복확인] 시작 - 이메일: {}", email);

        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent()) {
            log.error("❌ [이메일 중복확인] 이메일 중복 - {}", email);
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        log.info("✅ [이메일 중복확인] 사용 가능한 이메일 - {}", email);
        return "사용 가능한 이메일입니다.";
    }

    // 토큰 재발급
    @Transactional
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

    @Transactional
    public String sendPasswordResetEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        Optional<PasswordResetToken> existToken = passwordResetTokenRepository.findByUser(user);
        if (existToken.isPresent()) {
            PasswordResetToken passwordResetToken = existToken.get();
            passwordResetTokenRepository.delete(passwordResetToken);
            passwordResetTokenRepository.flush();
        }

        String token = generateSecureUniqueNumericCode();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return "비밀번호 재설정 인증을 위한 6자리 숫자코드가 이메일로 발송되었습니다.";
    }

    private String generateSecureUniqueNumericCode() {
        SecureRandom random = new SecureRandom();
        String token;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(random.nextInt(10)); // 0-9만 사용
            }
            token = sb.toString();
        } while (passwordResetTokenRepository.existsByToken(token));

        return token;
    }

    // Cookie 설정 헬퍼 메서드
    private void setCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = jwtTokenProvider.createResponseCookie(
                JwtTokenProvider.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                60 * 60 * 24 // 1일
        );

        ResponseCookie refreshTokenCookie = jwtTokenProvider.createResponseCookie(
                JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                60 * 60 * 24 * 7 // 7일
        );

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        log.info("🍪 [쿠키 설정] AccessToken 쿠키: {}", accessTokenCookie.toString());
        log.info("🍪 [쿠키 설정] RefreshToken 쿠키: {}", refreshTokenCookie.toString());
    }
}