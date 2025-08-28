package novaminds.gradproj.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.service.security.auth.AuthenticationHelper;
import novaminds.gradproj.domain.member.service.security.jwt.JwtLoginProcessor;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.member.entity.SocialType;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.MemberInterestCategory;
import novaminds.gradproj.domain.member.repository.MemberInterestCategoryRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.service.security.auth.AuthRedisService;
import novaminds.gradproj.domain.member.service.security.auth.PrincipalDetails;
import novaminds.gradproj.domain.member.service.security.jwt.JwtCookieUtil;
import novaminds.gradproj.domain.member.service.security.jwt.JwtTokenProvider;
import novaminds.gradproj.global.service.S3Service;
import novaminds.gradproj.domain.member.web.dto.AuthRequest;
import novaminds.gradproj.domain.member.web.dto.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.Duration;
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
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthRedisService authRedisService;
    private final MemberOnboardingService memberOnboardingService;
    private final JwtLoginProcessor jwtLoginProcessor;
    private final AuthenticationHelper authenticationHelper;

    // 랜덤 인증번호 생성용 정적 필드
    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public AuthResponse.SignupResponse signup(AuthRequest.SignupRequest request, HttpServletResponse response) {

        // 이메일 중복 확인
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        // loginId 생성 (LOCAL_UUID앞8자리)
        String loginId = "LOCAL_" + UUID.randomUUID().toString().substring(0, 8);

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

        memberOnboardingService.setupDefaultResources(savedMember);

        authenticationHelper.setAuthentication(savedMember);
        
        Authentication authentication = authenticationHelper.createAuthentication(savedMember);

        jwtLoginProcessor.processLogin(response, authentication);

        return AuthResponse.SignupResponse.from(savedMember);
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
            } catch (Exception e) {
                log.error("❌ [추가 정보 입력] 프로필 이미지 업로드 실패", e);
                throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.", e);
            }
        }


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

        return AuthResponse.AdditionalInfoResponse.from(member);
    }

    // 로그인
    @Transactional
    public AuthResponse.LoginResponse login(AuthRequest.LoginRequest request, HttpServletResponse response) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorStatus.EMAIL_PW_NOT_MACTHED));

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getLoginId(), request.getPassword())
        );

        authenticationHelper.setAuthentication((PrincipalDetails) authentication.getPrincipal());

        jwtLoginProcessor.processLogin(response, authentication);

        return AuthResponse.LoginResponse.from(member);
    }

    /**
     * 사용자 로그아웃 처리
     * 현재 사용 중인 액세스/리프레시 토큰을 블랙리스트에 추가하여 재사용 방지
     *
     * @param request  HTTP 요청 (쿠키에서 토큰 추출용)
     * @param response HTTP 응답 (쿠키 제거용)
     */
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 토큰 추출
        String accessToken = jwtCookieUtil.resolveToken(request, "accessToken").orElse(null);
        String refreshToken = jwtCookieUtil.resolveToken(request, "refreshToken").orElse(null);

        jwtLoginProcessor.processLogout(response, accessToken, refreshToken);
    }

    public String checkEmailDuplication(String email) {
        // 이메일 중복 확인
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
        return "사용 가능한 이메일입니다.";
    }

    // 토큰 재발급
    @Transactional
    public void refreshToken(String refreshToken, HttpServletResponse response) {

        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String loginId = jwtTokenProvider.getLoginIdFromToken(refreshToken);
        Member member = memberRepository.findById(loginId)
                .orElseThrow(() -> {
                    log.error("❌ [토큰 재발급] 사용자 없음 - loginId: {}", loginId);
                    return new IllegalArgumentException("사용자를 찾을 수 없습니다.");
                });

        // 새 토큰 생성
        authenticationHelper.setAuthentication(member);
        
        Authentication authentication = authenticationHelper.createAuthentication(member);

        jwtLoginProcessor.processLogin(response, authentication);
    }

    /**
     * 비밀번호 재설정을 위한 인증 코드 발송
     * 6자리 숫자 코드를 생성하여 Redis에 저장 후 이메일로 전송
     *
     * @param email 비밀번호를 재설정할 사용자의 이메일 주소
     * @return      성공 메시지
     */
    @Transactional
    public String sendPasswordResetEmail(String email) {
        
        // 이메일로 사용자 존재 여부 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 기존 Redis에 저장된 인증 코드가 있다면 삭제 (중복 발송 방지)
        String existingToken = authRedisService.getPasswordResetToken(email);
        if (existingToken != null) {
            authRedisService.deletePasswordResetToken(email);
        }

        // 6자리 랜덤 숫자 코드 생성
        String token = String.format("%06d", secureRandom.nextInt(1000000));
        
        // Redis에 24시간 TTL로 저장
        authRedisService.savePasswordResetToken(email, token, Duration.ofHours(24));
        
        // 이메일 발송
        emailService.sendPasswordResetEmail(email, token);

        return "비밀번호 재설정 인증을 위한 6자리 숫자코드가 이메일로 발송되었습니다.";
    }
    
    /**
     * 비밀번호 재설정 인증 코드 확인
     * Redis에 저장된 코드와 사용자 입력 코드를 비교 후 일치하면 삭제
     *
     * @param email          사용자의 이메일 주소
     * @param inputToken     사용자가 입력한 인증 코드
     * @return               인증 성공 여부
     */
    public boolean verifyPasswordResetToken(String email, String inputToken) {
        
        // Redis에서 저장된 코드 조회
        String storedToken = authRedisService.getPasswordResetToken(email);
        
        // 저장된 코드가 없거나 일치하지 않는 경우
        if (storedToken == null || !storedToken.equals(inputToken)) {
            log.warn("❌ [비밀번호 재설정] 인증 코드 불일치 - email: {}", email);
            return false;
        }
        
        // 인증 성공 시 Redis에서 코드 삭제 (일회성 코드)
        authRedisService.deletePasswordResetToken(email);
        
        return true;
    }
}