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
import novaminds.gradproj.global.template.CursorPagingHelper;
import novaminds.gradproj.global.template.CursorResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorSkinQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;

    private final CursorPagingHelper cursorPagingHelper;

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

        // 페이징 로직 처리
        CursorResult<RefrigeratorSkin> pageResult = cursorPagingHelper.getPage(
                (size) -> refrigeratorSkinRepository.findSkinsWithCursor(cursorId, size),
                RefrigeratorSkin::getId,
                DEFAULT_PAGE_SIZE
        );

        List<RefrigeratorSkin> skins = pageResult.content();

        // 배치로 소유 상태 조회
        List<Long> skinIds = skins.stream().map(RefrigeratorSkin::getId).toList();
        Map<Long, SkinOwnershipStatus> skinOwnershipMap = getBatchSkinOwnershipStatus(memberId, skinIds);

        // DTO List 변환
        var skinResponses = skins.stream()
                .map(skin -> {
                    SkinOwnershipStatus status = skinOwnershipMap.get(skin.getId());
                    return RefrigeratorConverter.toRefrigeratorSkinListResponse(skin, status.owned(), status.equipped());
                })
                .toList();

        // 무한 스크롤용 반환 DTO 생성
        return RefrigeratorConverter.toRefrigeratorSkinsPageResponse(
                skinResponses,
                pageResult.nextCursor(),
                pageResult.hasNext()
        );
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

        // 페이징 로직 처리
        CursorResult<MemberRefrigeratorSkin> pageResult = cursorPagingHelper.getPage(
                (size) -> memberRefrigeratorSkinRepository.findOwnedSkinsWithCursor(memberId, cursorId, size),
                MemberRefrigeratorSkin::getId,
                DEFAULT_PAGE_SIZE
        );

        List<MemberRefrigeratorSkin> ownedSkins = pageResult.content();
        
        // DTO로 변환
        var skinResponses = ownedSkins.stream()
                .map(memberSkin -> RefrigeratorConverter.toRefrigeratorSkinListResponse(
                        memberSkin.getSkin(), 
                        true, // 보유 스킨이므로 항상 true
                        memberSkin.isEquipped()
                ))
                .toList();
        
        // 페이징 응답 객체 생성
        return RefrigeratorConverter.toRefrigeratorSkinsPageResponse(
                skinResponses,
                pageResult.nextCursor(),
                pageResult.hasNext()
        );
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

        // 멤버의 스킨 리스트에서 조회
        Optional<MemberRefrigeratorSkin> memberSkinOpt
                = memberRefrigeratorSkinRepository.findByMemberLoginIdAndSkinId(memberId, skinId);

        // 구매 여부 확인
        boolean owned = memberSkinOpt.isPresent();

        // 장착 여부 확인
        boolean equipped = owned && memberSkinOpt.get().isEquipped();

        return new SkinOwnershipStatus(owned, equipped);
    }

    /**
     * 특정 회원의 여러 냉장고 스킨에 대한 소유 및 장착 상태를 배치로 조회하여 Map으로 반환
     *
     * @param memberId 스킨을 조회하는 회원의 ID
     * @param skinIds  조회할 냉장고 스킨의 ID 목록
     * @return         {@code skinId}를 키로, {@code SkinOwnershipStatus}를 값으로 하는 Map
     */
    private Map<Long, SkinOwnershipStatus> getBatchSkinOwnershipStatus(String memberId, List<Long> skinIds) {
        // 배치로 멤버의 스킨 소유 정보 조회
        Map<Long, MemberRefrigeratorSkin> memberSkinMap = 
                memberRefrigeratorSkinRepository.findByMemberLoginIdAndSkinIds(memberId, skinIds);

        // 각 스킨에 대한 소유 및 장착 상태를 Map으로 변환
        return skinIds.stream()
                .collect(Collectors.toMap(
                        skinId -> skinId,
                        skinId -> {
                            MemberRefrigeratorSkin memberSkin = memberSkinMap.get(skinId);
                            if (memberSkin != null) {
                                return new SkinOwnershipStatus(true, memberSkin.isEquipped());
                            } else {
                                return new SkinOwnershipStatus(false, false);
                            }
                        }
                ));
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
