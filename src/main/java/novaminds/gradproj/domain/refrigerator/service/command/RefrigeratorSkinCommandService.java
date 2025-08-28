package novaminds.gradproj.domain.refrigerator.service.command;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.entity.Role;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkinImage;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinImageRepository;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorSkinCommandService {

    private final MemberRepository memberRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final RefrigeratorSkinImageRepository refrigeratorSkinImageRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;

    /**
     * 요청값을 통해 새로운 냉장고 스킨을 등록하고 데이터베이스에 저장
     * <br>회원이 관리자 권한을 가지고 있는지 확인 후 스킨 등록
     *
     * @param memberId 스킨 등록하는 회원의 ID
     * @param request 생성할 냉장고 스킨의 상세 정보. 스킨 이름, 설명, 가격, 이미지 URL
     * @return 새로 생성된 냉장고 스킨의 ID
     * @throws GeneralException 회원이 관리자 권한을 가지고 있지 않은 경우 ({@code MEMBER_NOT_ADMIN})
     */
    public Long registerRefrigeratorSkin(String memberId, RefrigeratorRequestDTO.RefrigeratorSkinCreateRequest request) {

        // 관리자인지 권한 확인
        if (!memberRepository.existsByLoginIdAndRole(memberId, Role.ADMIN)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_ADMIN);
        }

        // 냉장고 스킨 생성
        RefrigeratorSkin refrigeratorSkin = RefrigeratorSkin.builder()
                .skinName(request.getSkinName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        // 저장한 스킨 가져오기
        RefrigeratorSkin savedRefrigeratorSkin = refrigeratorSkinRepository.save(refrigeratorSkin);

        // 스킨 이미지 url 가져오기
        List<String> imageUrls = request.getImageUrls();

        // 가져온 이미지 url로 스킨 이미지 엔티티 생성
        List<RefrigeratorSkinImage> skinImages = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> RefrigeratorSkinImage.builder()
                        .refrigeratorSkin(savedRefrigeratorSkin)
                        .imageUrl(imageUrls.get(i))
                        .imageOrder(i + 1)
                        .build())
                .toList();

        // 생성된 스킨 이미지 엔티티 모두 저장
        refrigeratorSkinImageRepository.saveAll(skinImages);

        // 스킨 이미지 엔티티와 스킨과의 연관관계 설정
        skinImages.forEach(savedRefrigeratorSkin::addRefrigeratorSkinImage);

        return savedRefrigeratorSkin.getId();
    }

    /**
     * 냉장고 스킨 구매
     * <br>회원이 충분한 포인트를 가지고 있는지, 스킨을 이미 소유하고 있는지 확인
     * <br>구매할 때는 포인트를 차감하고 스킨을 회원에게 연결한 뒤, 해당 관계를 저장합니다.
     *
     * @param memberId 냉장고 스킨을 구매하는 회원의 ID
     * @param skinId 구매할 냉장고 스킨의 ID
     * @throws GeneralException
     * <ul>
     * <li>회원이 존재하지 않거나({@code MEMBER_NOT_FOUND})</li>
     * <li>냉장고 스킨이 존재하지 않거나({@code REFRIGERATOR_SKIN_NOT_FOUND})</li>
     * <li>스킨을 이미 소유하고 있거나({@code REFRIGERATOR_SKIN_ALREADY_OWNED})</li>
     * <li>회원의 포인트가 부족한 경우({@code INSUFFICIENT_POINTS})</li>
     * </ul>
     */
    public void purchaseRefrigeratorSkin(String memberId, Long skinId) {

        // 해당 냉장고 스킨의 구매 여부 확인
        boolean owned = memberRefrigeratorSkinRepository.existsByMemberLoginIdAndSkinId(memberId, skinId);
        if (owned) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_ALREADY_OWNED);
        }

        // 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 스킨 조회
        RefrigeratorSkin refrigeratorSkin = refrigeratorSkinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_NOT_FOUND));

        // 포인트 부족한지 점검
        if (member.getPoint() < refrigeratorSkin.getPrice()) {
            throw new GeneralException(ErrorStatus.INSUFFICIENT_POINTS);
        }

        // 회원과 스킨 연결
        MemberRefrigeratorSkin memberRefrigeratorSkin = RefrigeratorConverter.toMemberRefrigeratorSkin(member, refrigeratorSkin);

        // 회원의 냉장고 구매 관계 저장
        memberRefrigeratorSkinRepository.save(memberRefrigeratorSkin);

        // 양방향 연관관계 설정
        member.getPurchasedSkins().add(memberRefrigeratorSkin);

        // 냉장고 스킨의 가격 만큼 포인트 차감
        member.usePoint(refrigeratorSkin.getPrice());
    }

    /**
     * 회원의 냉장고 스킨을 장착
     * <p>
     * 현재 장착된 스킨을 해제하고, 새로운 스킨을 장착
     *
     * @param memberId 스킨을 장착할 회원의 ID
     * @param skinId   장착할 새로운 냉장고 스킨의 ID
     * @throws GeneralException
     * <ul>
     * <li>회원이 현재 장착 중인 스킨을 소유하고 있지 않거나({@code EQUIPPED_REFRIGERATOR_SKIN_NOT_OWNED})</li>
     * <li>새로 장착할 스킨을 이미 장착하고 있는 경우({@code ALREADY_EQUIPPED})</li>>
     * </ul>
     */
    public void equipSkin(String memberId, Long skinId) {

        // 새로 장착할 스킨 먼저 조회
        MemberRefrigeratorSkin skinToEquip = memberRefrigeratorSkinRepository
                .findByMemberLoginIdAndSkinId(memberId, skinId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_NOT_OWNED));

        // 이미 장착된 스킨이면 early return
        if (skinToEquip.isEquipped()) {
            throw new GeneralException(ErrorStatus.ALREADY_EQUIPPED);
        }

        // 현재 장착된 스킨 해제
        memberRefrigeratorSkinRepository.findByMemberLoginIdAndEquippedTrue(memberId)
                .ifPresent(MemberRefrigeratorSkin::unEquip);

        // 새 스킨 장착
        skinToEquip.equip();
    }
}
