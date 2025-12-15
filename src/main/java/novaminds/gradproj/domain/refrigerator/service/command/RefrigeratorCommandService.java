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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorCommandService {

    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 메시지 전송 도구

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
                .build();

        // 냉장고 저장
        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);

        // 회원과 냉장고 연관관계 생성
        member.setRefrigerator(savedRefrigerator);
    }

    /**
     * 냉장고에 여러 재료를 추가
     * 기존에 동일한 재료가 이미 존재하면 유통 기한 계산 후 유통 기한이 더 긴 것으로 사용
     *
     * @param member 재료를 보관할 회원
     * @param requests 추가할 재료 정보 리스트
     */
    public void addIngredientsToRefrigerator(
            Member member, List<RefrigeratorRequestDTO.IngredientItem> requests
    ) {
        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 각 재료를 순회하면서 추가
        for (RefrigeratorRequestDTO.IngredientItem request : requests) {
            addSingleIngredientToRefrigerator(refrigerator, request);
        }

        sendRefreshSignal(refrigerator.getId());
    }

    /**
     * 냉장고에 단일 재료를 추가 (내부 메서드)
     * 기존에 동일한 재료가 이미 존재하면 유통 기한 계산 후 유통 기한이 더 긴 것으로 사용
     *
     * @param refrigerator 재료를 보관할 냉장고
     * @param request 추가할 재료 정보
     */
    private void addSingleIngredientToRefrigerator(
            Refrigerator refrigerator, RefrigeratorRequestDTO.IngredientItem request
    ) {
        // 재료 조회
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND));

        // 새로 계산된 유통기한 (요청에 유통기한이 있으면 그것을 사용, 없으면 재료의 기본 유통기한 계산)
        LocalDate newExpirationDate = request.getExpirationDate() != null
                ? request.getExpirationDate()
                : calculateExpirationDate(ingredient, request.getStorageType());

        // 기존에 동일한 재료, 보관타입이 있는지 확인
        Optional<StoredItem> existingItemOpt = storedItemRepository.findByRefrigeratorIdAndIngredientIdAndStorageType(
                refrigerator.getId(), ingredient.getId(), request.getStorageType()
        );

        if (existingItemOpt.isEmpty()) {
            // 기존 냉장고에 존재하지 않는 재료의 경우 새로 추가
            StoredItem newItem = RefrigeratorConverter.toStoredItem(
                    refrigerator, ingredient, request.getQuantity(), newExpirationDate, request.getStorageType()
            );
            StoredItem savedItem = storedItemRepository.save(newItem);
            refrigerator.addStoredItem(savedItem);
        } else {
            // 기존 아이템이 있는 경우 유통기한 비교하여 더 긴 것으로 업데이트
            StoredItem existingItem = existingItemOpt.get();

            // 새로운 유통기한이 더 길면 기존 아이템의 유통기한 업데이트
            if (newExpirationDate.isAfter(existingItem.getExpirationDate())) {
                existingItem.updateExpirationDate(newExpirationDate);
            }

            // 기존 아이템에 수량 더하기
            existingItem.updateQuantity(existingItem.getQuantity() + request.getQuantity());
            // 기존에 재료가 존재하나 유통기한이 더 길거나 같으면 아무것도 하지 않음
        }
    }

    public Long modifyMyStoredItem(Member member, Long storedItemId, RefrigeratorRequestDTO.ModifyStoredItemRequest request) {

        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // StoredItem 조회
        StoredItem storedItem = storedItemRepository.findByIdAndRefrigeratorId(storedItemId, refrigerator.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND));

        // 버전 체크 (동시성 제어)
        if (!Objects.equals(storedItem.getVersion(), request.getVersion())) {
            throw new GeneralException(ErrorStatus.OPTIMISTIC_LOCK_ERROR);
        }

        // 변경된 필드만 업데이트
        storedItem.updateFieldIfChanged(request);

        sendRefreshSignal(refrigerator.getId());
        return storedItem.getId();
    }

    /**
     * 냉장고에 보관된 재료 삭제
     *
     * @param member 재료를 삭제할 회원
     * @param storedItemIds 삭제할 재료의 ID 리스트
     */
    public void removeMyIngredients(
            Member member,
            Set<Long> storedItemIds
    ) {
        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

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
        storedItemsToDelete.forEach(refrigerator::removeStoredItem);

        sendRefreshSignal(refrigerator.getId());
    }


    /**
     * 재료의 ShelfLife 정보와 보관 방식을 기반으로 유통기한을 계산.
     *
     * @param ingredient 재료 정보
     * @param storageType 보관 방식
     * @return 계산된 유통기한
     */
    public LocalDate calculateExpirationDate(Ingredient ingredient, StorageType storageType) {
        LocalDate today = LocalDate.now();

        int shelfLifeDays = switch (storageType) {
            case ROOM_TEMPERATURE -> ingredient.getShelfLife().getRoomTempDays();
            case REFRIGERATOR -> ingredient.getShelfLife().getFridgeDays();
            case FREEZER -> ingredient.getShelfLife().getFreezerDays();
        };

        return today.plusDays(shelfLifeDays);
    }

    /**
     * 해당 냉장고를 구독 중인 모든 사용자에게 새로고침을 위한 메시지 전송
     */
    private void sendRefreshSignal(Long refrigeratorId) {

        String destination = "/sub/refrigerator/" + refrigeratorId;
        SocketMessage message = new SocketMessage("INGREDIENT_UPDATE", "재료가 변경되었습니다.");

        messagingTemplate.convertAndSend(destination, message);
    }

    // 웹소켓 메시지용 내부 클래스
    public record SocketMessage(String type, String message) {}
}
