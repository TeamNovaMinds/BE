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
@RequestMapping("/api/refrigerators")
@RequiredArgsConstructor
@Tag(name = "냉장고 관련 API", description = "냉장고에 아이템 등록, 냉장고 스킨 등록, 구매, 조회 등등을 처리하는 API 입니다.")
public class RefrigeratorController {

    private final RefrigeratorSkinCommandService refrigeratorSkinCommandService;
    private final RefrigeratorSkinQueryService refrigeratorSkinQueryService;

    @Operation(summary = "냉장고 스킨 등록", description = "냉장고의 스킨을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/skins")
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
    @GetMapping("/skins")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse> getRefrigeratorSkins(
            @CurrentLoginId String memberId,
            @RequestParam(required = false) Long cursorId
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinQueryService.getRefrigeratorSkins(cursorId, memberId));
    }

    @Operation(summary = "냉장고 스킨 상세 조회", description = "특정 냉장고 스킨을 상세조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/skins/{skinId}")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinResponse> getRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @PathVariable Long skinId
    ) {
        return ApiResponse.onSuccess(refrigeratorSkinQueryService.getRefrigeratorSkin(skinId, memberId));
    }

    @Operation(summary = "특정 냉장고 스킨 구매", description = "냉장고의 스킨을 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "실패, 회원 존재 X"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SKIN4002", description = "실패, 스킨 존재 X"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SKIN4003", description = "실패, 스킨 이미 구매"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POINT4001", description = "실패, 포인트 부족"),
    })
    @PostMapping("/skins/{skinId}")
    public ApiResponse<String> purchaseRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @PathVariable Long skinId
    ) {
        refrigeratorSkinCommandService.purchaseRefrigeratorSkin(memberId, skinId);
        return ApiResponse.onSuccess("냉장고 스킨 구매가 완료되었습니다.");
    }
}
