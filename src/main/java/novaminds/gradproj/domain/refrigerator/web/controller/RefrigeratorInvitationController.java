package novaminds.gradproj.domain.refrigerator.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.refrigerator.service.command.RefrigeratorInvitationCommandService;
import novaminds.gradproj.domain.refrigerator.service.query.RefrigeratorInvitationQueryService;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refrigerators/invitations")
@RequiredArgsConstructor
@Tag(name = "냉장고 초대 관련 API", description = "냉장고 공유를 위한 초대 API입니다.")
public class RefrigeratorInvitationController {

    private final RefrigeratorInvitationCommandService invitationCommandService;
    private final RefrigeratorInvitationQueryService invitationQueryService;

    @Operation(summary = "냉장고 초대 보내기", description = "팔로잉 중인 사용자에게 냉장고 공유 초대를 보냅니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER402", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REFRIGERATOR401", description = "냉장고를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION405", description = "자기 자신을 초대할 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION406", description = "이미 같은 냉장고를 사용 중입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION402", description = "이미 초대를 보낸 사용자입니다.")
    })
    @PostMapping("/{nickname}/send")
    public ApiResponse<String> sendInvitation(
            @CurrentUser Member member,
            @PathVariable String nickname
    ) {
        invitationCommandService.sendInvitation(member, nickname);
        return ApiResponse.onSuccess("초대를 보냈습니다.");
    }

    @Operation(summary = "받은 초대 목록 조회", description = "대기 중인 받은 초대 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/received")
    public ApiResponse<RefrigeratorResponseDTO.InvitationListResponse> getReceivedInvitations(
            @CurrentUser Member member
    ) {
        RefrigeratorResponseDTO.InvitationListResponse response = invitationQueryService.getReceivedInvitations(member);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "보낸 초대 목록 조회", description = "대기 중인 보낸 초대 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/sent")
    public ApiResponse<RefrigeratorResponseDTO.InvitationListResponse> getSentInvitations(
            @CurrentUser Member member
    ) {
        RefrigeratorResponseDTO.InvitationListResponse response = invitationQueryService.getSentInvitations(member);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "냉장고 초대 수락", description = "받은 냉장고 공유 초대를 수락합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION401", description = "초대를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION404", description = "초대에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION403", description = "이미 처리된 초대입니다.")
    })
    @PostMapping("/{invitationId}/accept")
    public ApiResponse<String> acceptInvitation(
            @CurrentUser Member member,
            @PathVariable Long invitationId
    ) {
        invitationCommandService.acceptInvitation(member, invitationId);
        return ApiResponse.onSuccess("초대를 수락했습니다.");
    }

    @Operation(summary = "냉장고 초대 거절", description = "받은 냉장고 공유 초대를 거절합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION401", description = "초대를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION404", description = "초대에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION403", description = "이미 처리된 초대입니다.")
    })
    @PostMapping("/{invitationId}/reject")
    public ApiResponse<String> rejectInvitation(
            @CurrentUser Member member,
            @PathVariable Long invitationId
    ) {
        invitationCommandService.rejectInvitation(member, invitationId);
        return ApiResponse.onSuccess("초대를 거절했습니다.");
    }

    @Operation(summary = "냉장고 초대 취소", description = "보낸 냉장고 공유 초대를 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION401", description = "초대를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION404", description = "초대에 대한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "INVITATION403", description = "이미 처리된 초대입니다.")
    })
    @DeleteMapping("/{invitationId}/cancel")
    public ApiResponse<String> cancelInvitation(
            @CurrentUser Member member,
            @PathVariable Long invitationId
    ) {
        invitationCommandService.cancelInvitation(member, invitationId);
        return ApiResponse.onSuccess("초대를 취소했습니다.");
    }
}