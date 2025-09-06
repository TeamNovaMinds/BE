package novaminds.gradproj.domain.refrigerator.service.query;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import novaminds.gradproj.domain.refrigerator.repository.StoredItemRepository;
import novaminds.gradproj.domain.refrigerator.repository.projection.StorageTypeCount;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorQueryService {

    private final StoredItemRepository storedItemRepository;
    private final RefrigeratorCommandService refrigeratorCommandService;

    /**
     * 냉장고 속 재료 조회
     * 보관 방법에 따라 필터링하고, 키워드로 재료명 검색 가능
     *
     * @param member 조회할 회원
     * @param storageType 보관 방법 (ROOM_TEMPERATURE, REFRIGERATOR, FREEZER)
     * @param keyword 재료명 검색 키워드 (null일 경우 전체 조회)
     * @return 보관 중인 재료 목록 DTO
     */
    public RefrigeratorResponseDTO.IngredientResponse getMyStoredItems(
            Member member,
            StorageType storageType,
            String keyword
    ) {

        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 냉장고 속 저장된 재료들 조회, 보관 방법에 따라 조회
        List<StoredItem> storedItems = storedItemRepository.findStoredItems(
                refrigerator.getId(), storageType, keyword
        );

        // DTO 변환
        return RefrigeratorConverter.toIngredientResponse(storedItems);
    }

    /**
     * 냉장고 속 재료 개수 통계 조회
     * 보관 방법별(실온/냉장/냉동)로 재료 개수를 집계
     *
     * @param member 조회할 회원
     * @return 보관 방법별 재료 개수 DTO
     */
    public RefrigeratorResponseDTO.StoredIngredientCount getMyStoredItemsCount(Member member) {

        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        StorageTypeCount storageTypeCount = storedItemRepository.countByStorageTypes(refrigerator.getId());

        return RefrigeratorConverter.toStoredIngredientCount(storageTypeCount);
    }

    /**
     * 냉장고 속 특정 재료 1개 상세 조회
     * 재료 ID, 이름, 수량, 보관방법, 유통기한, D-Day 반환
     *
     * @param member 조회할 회원
     * @param storedItemId 조회할 저장된 재료의 ID
     * @return 저장된 재료의 상세 정보
     */
    public RefrigeratorResponseDTO.StoredIngredientResponse getMyStoredItem(Member member, Long storedItemId) {

        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        StoredItem storedItem = storedItemRepository.findByIdAndRefrigeratorId(storedItemId, refrigerator.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND));

        return RefrigeratorConverter.toStoredIngredientResponse(storedItem);
    }

    /**
     * StorageType 변경 시 새로 계산될 유통기한 미리보기
     * 프론트에서 미리보기 용도로 사용
     *
     * @param member 조회할 회원
     * @param storedItemId 저장된 재료 ID
     * @param storageType 새로운 보관 방식 (ROOM_TEMPERATURE, REFRIGERATOR, FREEZER)
     * @return 계산된 유통기한 (LocalDate)
     */
    public LocalDate previewExpirationDate(
            Member member, 
            Long storedItemId, 
            StorageType storageType
    ) {
        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // StoredItem 조회
        StoredItem storedItem = storedItemRepository.findByIdAndRefrigeratorId(storedItemId, refrigerator.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND));

        // 새로운 StorageType으로 유통기한 계산
        return refrigeratorCommandService.calculateExpirationDate(
                storedItem.getIngredient(), storageType
        );
    }
}
