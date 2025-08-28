package novaminds.gradproj.domain.refrigerator.service.query;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.refrigerator.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorSkinQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;

    /**
     * 특정 냉장고 스킨의 세부 정보를 조회하고 특정 회원의 구매 여부 확인 (단건 조회)
     *
     * @param memberId 스킨을 조회하는 회원의 ID
     * @param skinId 조회할 냉장고 스킨의 고유 식별자
     * @return 냉장고 스킨의 세부 정보와 지정된 회원의 소유 상태가 포함된 {@code RefrigeratorSkinResponse} 객체
     */
    public RefrigeratorResponseDTO.RefrigeratorSkinResponse getRefrigeratorSkin(String memberId, Long skinId) {

        // 냉장고 스킨 조회
        RefrigeratorSkin refrigeratorSkin = refrigeratorSkinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_NOT_FOUND));

        // 소유 여부 및 장착 상태 확인
        SkinOwnershipStatus status = getSkinOwnershipStatus(memberId, refrigeratorSkin.getId());

        // DTO 변환
        return RefrigeratorConverter.toRefrigeratorSkinResponse(refrigeratorSkin, status.owned(), status.equipped());
    }

    /**
     * 냉장고 스킨의 목록과 커서 기반 페이징을 위한 {@code nextCursor}, {@code hasNext} 필드를 같이 조회
     *
     * @param memberId 스킨을 조회하는 회원의 ID
     * @param cursorId 페이징을 위한 커서, null일 경우, 처음부터 조회를 시작
     *
     * @return 냉장고 스킨 목록과 페이징을 위한 정보를 포함하는 {@code RefrigeratorSkinsPageResponse} 객체
     */
    public RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse getRefrigeratorSkins(String memberId, Long cursorId) {

        // 페이지 크기 + 1로 조회하여 다음 페이지 존재 여부 확인
        List<RefrigeratorSkin> skins = refrigeratorSkinRepository.findSkinsWithCursor(cursorId, DEFAULT_PAGE_SIZE + 1);

        // 다음 페이지 존재 여부 판단
        boolean hasNext = skins.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            skins.removeLast(); // 마지막 요소 제거 (페이징에서 다음 요소 유무를 확인하기 위해 가져왔던 +1 추가 데이터 삭제)
            nextCursor = skins.getLast().getId(); // 다음 커서 값 설정
        }

        // DTO List 변환
        var skinResponses = skins.stream()
                .map(skin -> {
                    // 소유 여부 및 장착 상태 확인
                    SkinOwnershipStatus status = getSkinOwnershipStatus(memberId, skin.getId());
                    
                    // DTO 변환
                    return RefrigeratorConverter.toRefrigeratorSkinListResponse(skin, status.owned(), status.equipped());
                })
                .toList();

        // 무한 스크롤용 반환 DTO 생성
        return RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse.builder()
                .skins(skinResponses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 특정 회원이 소유한 냉장고 스킨의 목록과 커서 기반 페이징을 위한 {@code nextCursor}, {@code hasNext} 필드를 같이 조회
     *
     * @param memberId 스킨을 조회하는 회원의 ID
     * @param cursorId 페이징을 위한 커서, null일 경우, 처음부터 조회를 시작
     *
     * @return 소유한 냉장고 스킨 목록과 페이징을 위한 정보를 포함하는 {@code RefrigeratorSkinsPageResponse} 객체
     */
    public RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse getOwnedRefrigeratorSkins(String memberId, Long cursorId) {
        
        // 페이지 크기 + 1로 조회하여 다음 페이지 존재 여부 확인
        List<MemberRefrigeratorSkin> ownedSkins = memberRefrigeratorSkinRepository.findOwnedSkinsWithCursor(memberId, cursorId, DEFAULT_PAGE_SIZE + 1);
        
        // 다음 페이지 존재 여부 판단
        boolean hasNext = ownedSkins.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            ownedSkins.removeLast(); // 마지막 요소 제거 (페이징에서 다음 요소 유무를 확인하기 위해 가져왔던 +1 추가 데이터 삭제)
            nextCursor = ownedSkins.getLast().getId(); // 다음 커서 값 설정
        }
        
        // DTO로 변환
        var skinResponses = ownedSkins.stream()
                .map(memberSkin -> RefrigeratorConverter.toRefrigeratorSkinListResponse(
                        memberSkin.getSkin(), 
                        true, // 보유 스킨이므로 항상 true
                        memberSkin.isEquipped()
                ))
                .toList();
        
        // 페이징 응답 객체 생성
        return RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse.builder()
                .skins(skinResponses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 특정 회원의 냉장고 스킨에 대한 소유 및 장착 상태를 확인
     *
     * @param memberId 스킨을 조회하는 회원의 ID
     * @param skinId   냉장고 스킨의 ID
     *
     * @return         해당 회원의 냉장고 스킨에 대한 소유 상태({@code owned})와
     * 장착 상태({@code equipped})를 담고있는 {@code SkinOwnershipStatus} 객체
     */
    private SkinOwnershipStatus getSkinOwnershipStatus(String memberId, Long skinId) {
        // 구매 여부 확인
        boolean owned = memberRefrigeratorSkinRepository.existsByMemberLoginIdAndSkinId(memberId, skinId);

        // 장착 여부 확인
        boolean equipped = false;
        if (owned) {
            // owned가 true면 이미 존재하는 것이기 때문에 ifPresent를 수행하지 않고 바로 .get() 사용해도 무방
            MemberRefrigeratorSkin memberSkin = memberRefrigeratorSkinRepository.findByMemberLoginIdAndSkinId(memberId, skinId).get();
            equipped = memberSkin.isEquipped();
        }
        
        return new SkinOwnershipStatus(owned, equipped);
    }

    /**
     * <p>
     * 소유 여부와 장착 여부, 두 개의 {@code boolean} 값을 담고 있는 {@code record} 객체:
     * <ul>
     * <li>{@code owned}: 특정 스킨을 사용자가 소유하고 있는지 여부.</li>
     * <li>{@code equipped}: 소유한 스킨을 현재 사용자가 장착하고 있는지 여부.</li>
     * </ul>
     *
     * <p>
     * 주로 냉장고 스킨 조회 시 소유권 여부와 장착 여부를 조회하는 데 사용
     */
    private record SkinOwnershipStatus(boolean owned, boolean equipped) {}
}
