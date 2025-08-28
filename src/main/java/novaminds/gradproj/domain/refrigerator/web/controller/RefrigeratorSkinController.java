package novaminds.gradproj.domain.refrigerator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.service.security.auth.CurrentLoginId;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorSkinCommandService;
import novaminds.gradproj.domain.refrigerator.service.query.RefrigeratorSkinQueryService;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refrigerators/skins")
@RequiredArgsConstructor
@Tag(name = "냉장고 스킨 관련 API", description = "냉장고 스킨 등록, 구매, 조회 등등을 처리하는 API 입니다.")
public class RefrigeratorSkinController {

    private final RefrigeratorSkinCommandService refrigeratorSkinCommandService;
    private final RefrigeratorSkinQueryService refrigeratorSkinQueryService;

    @Operation(summary = "냉장고 스킨 등록", description = "냉장고의 스킨을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping
    public ApiResponse<Long> registerRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @RequestBody RefrigeratorRequestDTO.RefrigeratorSkinCreateRequest request
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinCommandService.registerRefrigeratorSkin(memberId, request));
    }

    @Operation(summary = "냉장고 스킨 리스트 조회", description = "냉장고 스킨을 리스트로 조회합니다..")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse> getRefrigeratorSkins(
            @CurrentLoginId String memberId,
            @RequestParam(required = false) Long cursorId
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinQueryService.getRefrigeratorSkins(memberId, cursorId));
    }

    @Operation(summary = "냉장고 스킨 상세 조회", description = "특정 냉장고 스킨을 상세조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/{skinId}")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinResponse> getRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @PathVariable Long skinId
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinQueryService.getRefrigeratorSkin(memberId, skinId));
    }

    @Operation(summary = "특정 냉장고 스킨 구매", description = "냉장고의 스킨을 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "실패, 회원 존재 X"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SKIN4002", description = "실패, 스킨 존재 X"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SKIN4003", description = "실패, 스킨 이미 구매"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POINT4001", description = "실패, 포인트 부족"),
    })
    @PostMapping("/{skinId}")
    public ApiResponse<Long> purchaseRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @PathVariable Long skinId
    ) {
        refrigeratorSkinCommandService.purchaseRefrigeratorSkin(memberId, skinId);
        return ApiResponse.onSuccess(skinId);
    }

    @Operation(summary = "보유 중인 냉장고 스킨 리스트 조회", description = "회원이 구매해서 보유 중인 냉장고 스킨을 리스트로 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/owned")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse> getOwnedRefrigeratorSkins(
            @CurrentLoginId String memberId,
            @RequestParam(required = false) Long cursorId
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinQueryService.getOwnedRefrigeratorSkins(memberId, cursorId));
    }

    @Operation(summary = "소유 중인 냉장고 중에서 특정 냉장고 장착", description = "선택해서 요청한 특정 냉장고를 장착합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/{skinId}/equipped")
    public ApiResponse<Long> equipRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @PathVariable Long skinId
    ) {
        refrigeratorSkinCommandService.equipSkin(memberId, skinId);
        return ApiResponse.onSuccess(skinId);
    }
}
