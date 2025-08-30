package novaminds.gradproj.domain.refrigerator.service.command;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.repository.StoredItemRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorCommandService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final StoredItemRepository storedItemRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * 회원에게 새로운 냉장고를 생성하고 연결하고 저장
     *
     * @param member 새로 생성된 냉장고와 연결할 회원 엔티티
     */
    public void createRefrigerator(Member member) {

        // 냉장고 생성
        Refrigerator refrigerator = Refrigerator.builder()
                .member(member)
                .build();

        // 냉장고 저장
        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);

        // 회원과 냉장고 연관관계 생성
        member.setRefrigerator(savedRefrigerator);
    }

    /**
     * 냉장고에 재료를 추가
     * 기존에 동일한 재료가 이미 존재하면 유통 기한 계산 후 유통 기한이 더 긴 것으로 사용
     *
     * @param member 재료를 보관할 회원
     * @param request 추가할 재료 정보
     * @return 추가된 재료 응답
     */
    public void addIngredientsToRefrigerator(
            Member member, RefrigeratorRequestDTO.AddIngredientRequest request
    ) {

        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 재료 ID들 수집
        List<Long> ingredientIds = request.getIngredients().stream()
                .map(RefrigeratorRequestDTO.IngredientItem::getIngredientId)
                .toList();

        // 배치 조회로 N+1 문제 해결
        List<Ingredient> ingredients = ingredientRepository.findAllById(ingredientIds);
        
        // Map으로 변환
        var ingredientMap = ingredients.stream()
                .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

        // 새로 추가하거나 업데이트할 StoredItem들 저장할 리스트
        List<StoredItem> itemsToSave = new ArrayList<>();
        
        for (var ingredientItem : request.getIngredients()) {
            Ingredient ingredient = ingredientMap.get(ingredientItem.getIngredientId());
            if (ingredient == null) {
                throw new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND);
            }
            
            // 새로 계산된 유통기한
            LocalDate newExpirationDate = calculateExpirationDate(ingredient, ingredientItem.getStorageType());
            
            // 기존에 동일한 재료, 보관타입이 있는지 확인
            Optional<StoredItem> existingItemOpt = storedItemRepository.findByRefrigeratorIdAndIngredientIdAndStorageType(
                    refrigerator.getId(), ingredient.getId(), ingredientItem.getStorageType()
            );
            
            if (existingItemOpt.isEmpty()) {
                // 기존 냉장고에 존재하지 않는 재료의 경우 새로 추가
                StoredItem newItem = RefrigeratorConverter.toStoredItem(
                        refrigerator, ingredient, newExpirationDate, ingredientItem.getStorageType()
                );
                itemsToSave.add(newItem);
            } else {
                // 기존 아이템이 있는 경우 유통기한 비교하여 더 긴 것으로 업데이트
                StoredItem existingItem = existingItemOpt.get();

                // 새로운 유통기한이 더 길면 기존 아이템 삭제 후 새로 추가
                if (newExpirationDate.isAfter(existingItem.getExpirationDate())) {

                    // 유통 기한을 새로 업데이트
                    existingItem.updateExpirationDate(newExpirationDate);
                }
                // 기존에 재료가 존재하나 유통기한이 더 길거나 같으면 아무것도 하지 않음
            }
        }
        
        // 새로 추가되거나 업데이트된 아이템들만 저장
        List<StoredItem> savedStoredItems = storedItemRepository.saveAll(itemsToSave);
        
        // Refrigerator 엔티티의 storedItems 리스트에 추가 (연관관계 관리)
        savedStoredItems.forEach(refrigerator::addStoredItems);
    }

    /**
     * 냉장고에 보관된 재료 삭제
     *
     * @param member 재료를 삭제할 회원
     * @param request 삭제할 재료의 ID를 담은 요청
     */
    public void removeMyIngredients(
            Member member,
            RefrigeratorRequestDTO.DeleteIngredient request
    ) {
        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 삭제할 재료 ID
        List<Long> storedItemIds = request.getStoredItemId();

        // 해당 StoredItem들이 실제로 이 냉장고에 속하는지 확인
        List<StoredItem> storedItemsToDelete = storedItemRepository.findAllById(storedItemIds);
        
        // 삭제 할 StoredItem이 해당 냉장고에 속하는지 검증
        List<StoredItem> invalidItems = storedItemsToDelete.stream()
                .filter(storedItem -> !storedItem.getRefrigerator().getId().equals(refrigerator.getId()))
                .toList();
        
        if (!invalidItems.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORED_ITEM_ACCESS_DENIED);
        }
        
        // 존재하지 않는 StoredItem ID가 있는지 확인
        if (storedItemsToDelete.size() != storedItemIds.size()) {
            throw new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND);
        }

        // StoredItem 삭제
        storedItemRepository.deleteAll(storedItemsToDelete);

        // Refrigerator 엔티티의 storedItems 리스트에서 제거
        storedItemsToDelete.forEach(refrigerator::removeStoredItems);
    }


    /**
     * 재료의 ShelfLife 정보와 보관 방식을 기반으로 유통기한을 계산.
     *
     * @param ingredient 재료 정보
     * @param storageType 보관 방식
     * @return 계산된 유통기한
     */
    private LocalDate calculateExpirationDate(Ingredient ingredient, StorageType storageType) {
        LocalDate today = LocalDate.now();

        int shelfLifeDays = switch (storageType) {
            case ROOM_TEMPERATURE -> ingredient.getShelfLife().getRoomTempDays();
            case REFRIGERATOR -> ingredient.getShelfLife().getFridgeDays();
            case FREEZER -> ingredient.getShelfLife().getFreezerDays();
        };

        return today.plusDays(shelfLifeDays);
    }
}
