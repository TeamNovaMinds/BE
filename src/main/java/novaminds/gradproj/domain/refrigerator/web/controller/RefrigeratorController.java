package novaminds.gradproj.domain.refrigerator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import novaminds.gradproj.domain.refrigerator.service.query.RefrigeratorQueryService;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api/refrigerators")
@RequiredArgsConstructor
@Tag(name = "냉장고 재료 관련 API", description = "냉장고 속 재료 등록 및 조회하는 API 입니다.")
public class RefrigeratorController {

    private final RefrigeratorCommandService refrigeratorCommandService;
    private final RefrigeratorQueryService refrigeratorQueryService;

    @Operation(summary = "냉장고에 재료 추가", description = "사용자의 냉장고에 재료를 추가합니다. 한 번에 여러 개의 재료를 추가할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @PostMapping("/stored-items")
    public ApiResponse<String> addIngredientsToRefrigerator(
            @CurrentUser Member member,
            @Valid @RequestBody RefrigeratorRequestDTO.IngredientItemList request
    ) {
        refrigeratorCommandService.addIngredientsToRefrigerator(member, request.getItems());
        return ApiResponse.onSuccess("재료 추가가 완료되었습니다.");
    }

    @Operation(summary = "냉장고 속 재료 조회", description = "사용자의 냉장고에 보관 중인 재료들을 조회합니다. 보관 방법에 따라 조회 재료를 필터링할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @GetMapping("/stored-items")
    public ApiResponse<RefrigeratorResponseDTO.IngredientResponse> getMyStoredItems(
            @CurrentUser Member member,
            @RequestParam StorageType storageType,
            @RequestParam(required = false) String keyword
    ) {
        var response = refrigeratorQueryService.getMyStoredItems(member, storageType, keyword);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "냉장고 속 특정 재료 1개 조회", description = "냉장고에 보관 중인 특정한 재료 1개의 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @GetMapping("/stored-items/{storedItemId}")
    public ApiResponse<RefrigeratorResponseDTO.StoredIngredientResponse> getMyStoredItem(
            @CurrentUser Member member,
            @PathVariable Long storedItemId
    ) {
        var myIngredient = refrigeratorQueryService.getMyStoredItem(member, storedItemId);
        return ApiResponse.onSuccess(myIngredient);
    }

    @Operation(summary = "냉장고 속 특정 재료 1개 수정", description = "냉장고에 보관 중인 특정한 재료 1개의 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @PutMapping("/stored-items/{storedItemId}")
    public ApiResponse<Long> modifyMyStoredItem(
            @CurrentUser Member member,
            @PathVariable Long storedItemId,
            @Valid @RequestBody RefrigeratorRequestDTO.ModifyStoredItemRequest request
    ) {
        var modifiedMyStoredItemId = refrigeratorCommandService.modifyMyStoredItem(member, storedItemId, request);
        return ApiResponse.onSuccess(modifiedMyStoredItemId);
    }

    @Operation(summary = "냉장고 속 재료 삭제", description = "사용자의 냉장고에 보관 중인 재료를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @DeleteMapping("/stored-items")
    public ApiResponse<String> removeMyIngredients(
            @CurrentUser Member member,
            @RequestParam("ids") Set<Long> storedItemIds
    ) {
        refrigeratorCommandService.removeMyIngredients(member, storedItemIds);
        return ApiResponse.onSuccess("재료가 성공적으로 삭제되었습니다.");
    }

    @Operation(summary = "보관 방법 변경 시 유통기한 미리보기", description = "보관 방법을 변경할 때 새로 계산될 유통기한을 미리 확인합니다. 실제 데이터는 변경되지 않습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR403", description = "냉장고 재료를 찾을 수 없습니다."),
    })
    @GetMapping("/stored-items/{storedItemId}/preview-expiration")
    public ApiResponse<LocalDate> previewExpirationDate(
            @CurrentUser Member member,
            @PathVariable Long storedItemId,
            @RequestParam StorageType storageType
    ) {
        var newExpirationDate = refrigeratorQueryService.previewExpirationDate(member, storedItemId, storageType);
        return ApiResponse.onSuccess(newExpirationDate);
    }

    @Operation(summary = "냉장고 속 재료 개수 확인", description = "사용자의 냉장고에 보관 중인 재료의 개수를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @GetMapping("/stored-items/count")
    public ApiResponse<RefrigeratorResponseDTO.StoredIngredientCount> getMyIngredientCount(
            @CurrentUser Member member
    ) {
        var myIngredientCount = refrigeratorQueryService.getMyStoredItemsCount(member);
        return ApiResponse.onSuccess(myIngredientCount);
    }
}
