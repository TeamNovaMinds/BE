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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/refrigerators")
@RequiredArgsConstructor
@Tag(name = "냉장고 재료 관련 API", description = "냉장고 속 재료 등록 및 조회하는 API 입니다.")
public class RefrigeratorController {

    private final RefrigeratorCommandService refrigeratorCommandService;
    private final RefrigeratorQueryService refrigeratorQueryService;

    @Operation(summary = "냉장고에 재료 추가", description = "사용자의 냉장고에 재료를 추가합니다. 여러 개의 재료를 한 번에 추가할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @PostMapping("/ingredients")
    public ApiResponse<String> addIngredientsToRefrigerator(
            @CurrentUser Member member,
            @Valid @RequestBody RefrigeratorRequestDTO.AddIngredientRequest request
    ) {
        refrigeratorCommandService.addIngredientsToRefrigerator(member, request);
        return ApiResponse.onSuccess("재료 추가가 완료되었습니다.");
    }

    @Operation(summary = "냉장고 속 재료 조회", description = "사용자의 냉장고에 보관 중인 재료들을 조회합니다. 보관 방법에 따라 조회 재료를 필터링할 수 있습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @GetMapping("/ingredients")
    public ApiResponse<RefrigeratorResponseDTO.IngredientResponse> getMyIngredients(
            @CurrentUser Member member,
            @RequestParam(required = false) StorageType storageType
    ) {
        var response = refrigeratorQueryService.getMyIngredients(member, storageType);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "냉장고 속 재료 삭제", description = "사용자의 냉장고에 보관 중인 재료를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INGREDIENT401", description = "재료를 찾을 수 없습니다."),
    })
    @DeleteMapping("/ingredients")
    public ApiResponse<String> removeMyIngredients(
            @CurrentUser Member member,
            @RequestParam("ids") Set<Long> storedItemIds
    ) {
        refrigeratorCommandService.removeMyIngredients(member, storedItemIds);
        return ApiResponse.onSuccess("재료가 성공적으로 삭제되었습니다.");
    }
}
