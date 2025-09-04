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
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorQueryService {

    private final StoredItemRepository storedItemRepository;

    public RefrigeratorResponseDTO.IngredientResponse getMyIngredients(Member member, StorageType storageType) {

        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 냉장고 속 저장된 재료들 조회, 보관 방법에 따라 조회
        List<StoredItem> storedItems = storedItemRepository.findStoredItems(
                refrigerator.getId(), storageType
        );

        // DTO 변환
        return RefrigeratorConverter.toIngredientResponse(storedItems);
    }

    public RefrigeratorResponseDTO.StoredIngredientCount getMyIngredientCount(Member member) {

        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        StorageTypeCount storageTypeCount = storedItemRepository.countByStorageTypes(refrigerator.getId());

        return RefrigeratorConverter.toStoredIngredientCount(storageTypeCount);
    }
}
