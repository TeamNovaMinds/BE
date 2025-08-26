package novaminds.gradproj.domain.member.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.handler.RefrigeratorSkinHandler;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.member.entity.PasswordResetToken;
import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.member.entity.SocialType;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.MemberInterestCategory;
import novaminds.gradproj.domain.member.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.member.repository.PasswordResetTokenRepository;
import novaminds.gradproj.domain.member.repository.MemberInterestCategoryRepository;
import novaminds.gradproj.domain.member.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import novaminds.gradproj.domain.member.service.security.jwt.JwtTokenProvider;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import novaminds.gradproj.global.service.S3Service;
import novaminds.gradproj.domain.member.web.dto.AuthRequest;
import novaminds.gradproj.domain.member.web.dto.AuthResponse;
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

    private final MemberRepository memberRepository;
    private final MemberInterestCategoryRepository memberInterestCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefrigeratorCommandService refrigeratorCommandService;

    @Transactional
    public AuthResponse.SignupResponse signup(AuthRequest.SignupRequest request, HttpServletResponse response) {
        log.info("🔄 [회원가입] 시작 - 이메일: {}", request.getEmail());

        // 이메일 중복 확인
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            log.error("❌ [회원가입] 이메일 중복 - {}", request.getEmail());
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // loginId 생성 (LOCAL_UUID앞8자리)
        String loginId = generateLoginId(SocialType.LOCAL, null);
        log.info("✓ [회원가입] loginId 생성 완료: {}", loginId);

        // 임시 닉네임 생성
        String tempNickname = "user_" + UUID.randomUUID().toString().substring(0, 8);

        Member member = Member.builder()
                .loginId(loginId)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(tempNickname)
                .role(Role.USER)
                .socialType(SocialType.LOCAL)
                .isProfileCompleted(false)
                .build();

        Member savedMember = memberRepository.save(member);
        log.info("✅ [회원가입] 기본 정보 저장 완료 - loginId: {}, email: {}", savedMember.getLoginId(), savedMember.getEmail());

        createRefrigeratorForUser(savedMember);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(savedMember), null, new PrincipalDetails(savedMember).getAuthorities()
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

        return AuthResponse.SignupResponse.from(savedMember);
    }

    private void createRefrigeratorForUser(Member member) {
        // 냉장고 생성
        refrigeratorCommandService.createRefrigerator(member);

        // 기본 스킨 찾기
        RefrigeratorSkin defaultSkin = refrigeratorSkinRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new RefrigeratorSkinHandler(ErrorStatus.DEFAULT_REFRIGERATOR_SKIN_NOT_FOUND));

        // 기본 스킨을 유저에게 부여하고 장착
        MemberRefrigeratorSkin userSkin = MemberRefrigeratorSkin.builder()
                .member(member)
                .skin(defaultSkin)
                .isEquipped(true)
                .build();

        memberRefrigeratorSkinRepository.save(userSkin);

        log.info("✅ [회원가입] 냉장고 및 기본 스킨 생성 완료 - userId: {}", member.getLoginId());
    }

    // 추가 정보 입력 (닉네임, 프로필 이미지)
    @Transactional
    public AuthResponse.AdditionalInfoResponse completeProfilePart1(
            Member member,
            AuthRequest.AdditionalInfoNicknameRequest request,
            MultipartFile profileImage
    ) {

        // 닉네임 중복 확인 (현재 사용자의 닉네임과 다른 경우에만)
        if (!member.getNickname().equals(request.getNickname()) &&
                memberRepository.findByNickname(request.getNickname()).isPresent()) {
            log.error("❌ [추가 정보 입력] 닉네임 중복 - {}", request.getNickname());
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 닉네임 업데이트
        member.updateNickname(request.getNickname());

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                if (member.getProfileImage() != null && member.getProfileImage().contains("amazonaws.com")) {
                    s3Service.deleteFile(member.getProfileImage());
                }

                String profileImgUrl = s3Service.uploadFile(profileImage, "profile");
                member.updateProfileImage(profileImgUrl);
                log.info("✓ [추가 정보 입력] 프로필 이미지 업로드 완료 - URL: {}", profileImgUrl);
            } catch (Exception e) {
                log.error("❌ [추가 정보 입력] 프로필 이미지 업로드 실패", e);
                throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.", e);
            }
        }

        log.info("✅ [추가 정보 입력] 완료 - loginId: {}, 닉네임: {}, 프로필 이미지: {}",
                member.getLoginId(), request.getNickname(), member.getProfileImage() != null ? "있음" : "없음");

        return AuthResponse.AdditionalInfoResponse.from(member);
    }

    // 추가 정보 입력 (닉네임, 프로필 이미지)
    @Transactional
    public AuthResponse.AdditionalInfoResponse completeProfilePart2(
            Member member,
            AuthRequest.AdditionalInfoInterestRequest request
    ) {
        // 기존 관심 카테고리 삭제
        memberInterestCategoryRepository.deleteByMemberLoginId(member.getLoginId());

        // 새로운 관심 카테고리 저장
        List<RecipeCategory> categories = request.getInterestCategories();
        for (RecipeCategory category : categories) {
            MemberInterestCategory interestCategory = MemberInterestCategory.create(member, category);
            memberInterestCategoryRepository.save(interestCategory);
        }

        // 프로필 완료 상태로 변경
        member.completeProfile();

        log.info("✅ [추가 정보 입력] 완료 - loginId: {}, 관심 카테고리 수: {}",
                member.getLoginId(), categories.size());

        return AuthResponse.AdditionalInfoResponse.from(member);
    }

    // 로그인
    @Transactional
    public AuthResponse.LoginResponse login(AuthRequest.LoginRequest request, HttpServletResponse response) {
        log.info("🔄 [로그인] 시작 - 이메일: {}", request.getEmail());

        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("❌ [로그인] 사용자 없음 - {}", request.getEmail());
                    return new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
                });

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getLoginId(), request.getPassword())
        );
        log.info("✓ [로그인] 인증 성공 - loginId: {}", member.getLoginId());

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
                member.getLoginId(), member.getEmail(), member.isProfileCompleted());

        return AuthResponse.LoginResponse.from(member);
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
        if (memberRepository.findByEmail(email).isPresent()) {
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
        Member member = memberRepository.findById(loginId)
                .orElseThrow(() -> {
                    log.error("❌ [토큰 재발급] 사용자 없음 - loginId: {}", loginId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        // 새 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(member), null, new PrincipalDetails(member).getAuthorities()
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

    public AuthResponse.LoginResponse getProfile(Member member) {
        return AuthResponse.LoginResponse.from(member);
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

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        Optional<PasswordResetToken> existToken = passwordResetTokenRepository.findByMember(member);
        if (existToken.isPresent()) {
            PasswordResetToken passwordResetToken = existToken.get();
            passwordResetTokenRepository.delete(passwordResetToken);
            passwordResetTokenRepository.flush();
        }

        String token = generateSecureUniqueNumericCode();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .member(member)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(member.getEmail(), token);

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