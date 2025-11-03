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
import novaminds.gradproj.domain.member.service.security.jwt.JwtCookieUtil;
import novaminds.gradproj.domain.member.web.dto.MemberRequestDTO;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final EmailService emailService;

    private final MemberRepository memberRepository;
    private final MemberInterestCategoryRepository memberInterestCategoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtCookieUtil jwtCookieUtil;
    private final AuthRedisService authRedisService;
    private final MemberOnboardingService memberOnboardingService;
    private final JwtLoginProcessor jwtLoginProcessor;
    private final AuthenticationHelper authenticationHelper;
    private final AuthenticationManager authenticationManager;

    // 랜덤 인증번호 생성용 정적 필드
    private static final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public MemberResponseDTO.SignupResponse signup(MemberRequestDTO.SignupRequest request, HttpServletResponse response) {

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
                .profileCompleted(false)
                .build();

        Member savedMember = memberRepository.save(member);

        memberOnboardingService.setupDefaultResources(savedMember);

        Authentication authentication = authenticationHelper.setAuthentication(savedMember);

        jwtLoginProcessor.issueAndSetTokens(response, authentication);

        return MemberResponseDTO.SignupResponse.from(savedMember);
    }


    // 추가 정보 입력 (닉네임, 프로필 이미지)
    @Transactional
    public MemberResponseDTO.AdditionalInfoResponse completeProfilePart1(
            Member member,
            MemberRequestDTO.AdditionalInfoNicknameRequest request
    ) {
        try {
            // 닉네임 업데이트
            member.updateNickname(request.getNickname());

            // 프로필 이미지 업데이트
            member.updateProfileImage(request.getProfileImgUrl());

            // 영속성 컨텍스트에서 DB로 정보 업데이트
            memberRepository.flush();

            return MemberResponseDTO.AdditionalInfoResponse.from(member);
        } catch (DataIntegrityViolationException e) {
            // DB constraint 위반 시 적절한 예외로 변환 - 여기서 위반할만한 건 닉네임 중복되는 예외밖에 없음
            throw new GeneralException(ErrorStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    // 추가 정보 입력 (관심 카테고리)
    @Transactional
    public MemberResponseDTO.AdditionalInfoResponse completeProfilePart2(
            Member member,
            MemberRequestDTO.AdditionalInfoInterestRequest request,
            HttpServletResponse response
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

        // 프로필 완성 후 새로운 JWT 토큰 발급 (profileCompleted=true)
        Authentication authentication = authenticationHelper.setAuthentication(member);
        jwtLoginProcessor.issueAndSetTokens(response, authentication);

        return MemberResponseDTO.AdditionalInfoResponse.from(member);
    }

    // 로그인
    @Transactional
    public MemberResponseDTO.LoginResponse login(MemberRequestDTO.LoginRequest request, HttpServletResponse response) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorStatus.EMAIL_PW_NOT_MATCHED));

        // 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getLoginId(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        jwtLoginProcessor.issueAndSetTokens(response, authentication);

        return MemberResponseDTO.LoginResponse.from(member);
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

    public String checkNicknameDuplication(String nickname) {
        // 닉네임 중복 확인
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new GeneralException(ErrorStatus.NICKNAME_ALREADY_EXISTS);
        }
        return "사용 가능한 닉네임입니다.";
    }

    /**
     * 비밀번호 재설정을 위한 인증 코드 발송
     * 6자리 숫자 코드를 생성하여 Redis에 저장 후 이메일로 전송
     *
     * @param email 사용자의 이메일
     * @return      성공 메시지
     */
    @Transactional
    public String sendPasswordResetEmail(String email) {

        // 이메일로 사용자 존재 여부 확인
        Optional<Member> maybeMember = memberRepository.findByEmail(email);

        // 해당 이메일이 가입되어있는지 안되어있는지 확인 못하게 하기 위해서
        // 이메일 존재 여부와 무관하게 동일한 응답을 반환하여 계정 유무 노출 방지
        if (maybeMember.isEmpty()) {
            return "비밀번호 재설정 인증을 위한 6자리 숫자코드가 이메일로 발송되었습니다.";
        }

        // 기존 Redis에 저장된 인증 코드가 있다면 삭제 (중복 발송 방지)
        String existingToken = authRedisService.getPasswordResetToken(email);
        if (existingToken != null) {
            authRedisService.deletePasswordResetToken(email);
        }

        // 6자리 랜덤 숫자 코드 생성
        String token = String.format("%06d", secureRandom.nextInt(1000000));
        
        // Redis에 15분 TTL로 저장
        authRedisService.savePasswordResetToken(email, token, Duration.ofMinutes(15));
        
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

    /**
     * 비밀번호 재설정 (인증 코드 검증 + 비밀번호 변경)
     * 인증 코드를 검증하고, 검증 성공 시 새로운 비밀번호로 변경
     *
     * @param request 이메일, 인증 코드, 새 비밀번호를 담은 요청 DTO
     * @return        비밀번호 재설정 성공 메시지
     */
    @Transactional
    public String resetPassword(MemberRequestDTO.PasswordResetConfirmRequest request) {

        // 1. 인증 코드 검증
        String storedToken = authRedisService.getPasswordResetToken(request.getEmail());

        if (storedToken == null || !storedToken.equals(request.getToken())) {
            log.warn("❌ [비밀번호 재설정] 인증 코드 불일치 - email: {}", request.getEmail());
            throw new GeneralException(ErrorStatus.INVALID_VERIFICATION_CODE);
        }

        // 2. 사용자 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 3. 소셜 로그인 사용자 체크 (소셜 로그인 사용자는 비밀번호 재설정 불가)
        if (member.getSocialType() != SocialType.LOCAL) {
            log.warn("❌ [비밀번호 재설정] 소셜 로그인 사용자 - email: {}, socialType: {}",
                    request.getEmail(), member.getSocialType());
            throw new GeneralException(ErrorStatus.SOCIAL_LOGIN_USER_CANNOT_RESET_PASSWORD);
        }

        // 4. 비밀번호 변경
        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        // 5. Redis에서 인증 코드 삭제 (일회성 코드)
        authRedisService.deletePasswordResetToken(request.getEmail());

        log.info("✅ [비밀번호 재설정] 성공 - email: {}", request.getEmail());

        return "비밀번호가 성공적으로 변경되었습니다.";
    }
}