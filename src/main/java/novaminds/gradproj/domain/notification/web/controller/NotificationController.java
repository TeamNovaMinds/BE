package novaminds.gradproj.domain.notification.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.notification.service.command.NotificationCommandService;
import novaminds.gradproj.domain.notification.service.query.NotificationQueryService;
import novaminds.gradproj.domain.notification.web.dto.NotificationResponseDTO;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 관리", description = "알림 조회 및 관리 API")
public class NotificationController {

	private final NotificationQueryService notificationQueryService;
	private final NotificationCommandService notificationCommandService;

	@Operation(summary = "알림 목록 조회",
			description = "사용자의 알림 목록을 페이징하여 조회합니다. 최신 알림이 먼저 표시됩니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다.")
	})
	@GetMapping
	public ApiResponse<NotificationResponseDTO.NotificationListResponse> getNotifications(
			@CurrentUser Member member,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		NotificationResponseDTO.NotificationListResponse response = notificationQueryService.getNotifications(member, page, size);
		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "읽지 않은 알림 개수 조회",
			description = "사용자의 읽지 않은 알림 개수를 조회합니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다.")
	})
	@GetMapping("/unread-count")
	public ApiResponse<NotificationResponseDTO.UnreadCountResponse> getUnreadCount(
			@CurrentUser Member member
	) {
		long unreadCount = notificationQueryService.getUnreadCount(member);
		NotificationResponseDTO.UnreadCountResponse response = NotificationResponseDTO.UnreadCountResponse.builder()
				.unreadCount(unreadCount)
				.build();
		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "알림 읽음 처리",
			description = "특정 알림을 읽음 상태로 변경합니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "읽음 처리 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다."),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "알림을 찾을 수 없습니다.")
	})
	@PatchMapping("/{notificationId}/read")
	public ApiResponse<String> markAsRead(
			@CurrentUser Member member,
			@PathVariable Long notificationId
	) {
		notificationCommandService.markAsRead(notificationId);
		return ApiResponse.onSuccess("알림을 읽음 처리했습니다.");
	}

	@Operation(summary = "모든 알림 읽음 처리",
			description = "사용자의 모든 알림을 읽음 상태로 변경합니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "전체 읽음 처리 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다.")
	})
	@PatchMapping("/read-all")
	public ApiResponse<String> markAllAsRead(
			@CurrentUser Member member
	) {
		notificationCommandService.markAllAsRead(member);
		return ApiResponse.onSuccess("모든 알림을 읽음 처리했습니다.");
	}
}
