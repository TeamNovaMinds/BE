package novaminds.gradproj.domain.member.service;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.handler.RefrigeratorSkinHandler;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberOnboardingService {

    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;
    private final RefrigeratorCommandService refrigeratorCommandService;

    /**
     * 전달된 회원에게 새로운 냉장고를 생성하고 기본 스킨 설정
     *
     * @param member 현재 로그인 중인 회원
     */
    @Transactional
    public void setupDefaultResources(Member member) {
        if (member.getRefrigerator() != null) {
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
                .equipped(true)
                .build();

        memberRefrigeratorSkinRepository.save(userSkin);
    }
}