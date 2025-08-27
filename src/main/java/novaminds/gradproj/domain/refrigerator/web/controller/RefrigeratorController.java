package novaminds.gradproj.domain.refrigerator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.service.security.auth.CurrentLoginId;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorCommandService;
import novaminds.gradproj.domain.refrigerator.service.query.RefrigeratorQueryService;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refrigerators")
@RequiredArgsConstructor
@Tag(name = "냉장고 관련 API", description = "냉장고에 아이템 등록, 냉장고 스킨 등록, 구매, 조회 등등을 처리하는 API 입니다.")
public class RefrigeratorController {

    private final RefrigeratorCommandService refrigeratorCommandService;
    private final RefrigeratorQueryService refrigeratorQueryService;

    @Operation(summary = "냉장고 스킨 등록", description = "냉장고의 스킨을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/skins")
    public ApiResponse<Long> registerRefrigeratorSkin(
            @CurrentLoginId String memberId,
            @RequestBody RefrigeratorRequestDTO.RefrigeratorSkinCreateRequest request
    ) {
        return ApiResponse.onSuccess(refrigeratorCommandService.registerRefrigeratorSkin(memberId, request));
    }

    @Operation(summary = "냉장고 스킨 리스트 조회", description = "냉장고 스킨을 리스트로 조회합니다..")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/skins")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse> getRefrigeratorSkins(
            @RequestParam(required = false) Long cursor
    ) {
        return ApiResponse.onSuccess(refrigeratorQueryService.getRefrigeratorSkins(cursor));
    }

    @Operation(summary = "냉장고 스킨 상세 조회", description = "특정 냉장고 스킨을 상세조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/skins/{skinId}")
    public ApiResponse<RefrigeratorResponseDTO.RefrigeratorSkinResponse> registerRefrigeratorSkin(
            @PathVariable Long skinId
    ) {
        return ApiResponse.onSuccess(refrigeratorQueryService.getRefrigeratorSkin(skinId));
    }
}
