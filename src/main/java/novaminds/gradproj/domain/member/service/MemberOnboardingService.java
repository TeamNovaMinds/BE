package novaminds.gradproj.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.handler.RefrigeratorSkinHandler;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.member.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberOnboardingService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;
    private final RefrigeratorCommandService refrigeratorCommandService;

    @Transactional
    public void setupDefaultResources(Member member) {
        if (refrigeratorRepository.existsByMember(member)) {
            log.info("ℹ️ 기존 사용자 {}의 냉장고가 이미 존재하므로 생성을 건너뜁니다.", member.getLoginId());
            return;
        }

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

        log.info("✅ [회원 온보딩] 냉장고 및 기본 스킨 생성 완료 - userId: {}", member.getLoginId());
    }
}