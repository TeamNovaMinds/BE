package novaminds.gradproj.domain.notification.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.notification.entity.NotificationType;
import novaminds.gradproj.domain.notification.service.command.NotificationCommandService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications/test")
@RequiredArgsConstructor
@Tag(name = "알림 테스트", description = "개발/테스트 환경에서만 사용 가능한 알림 테스트 API")
@Profile({"dev", "local"}) // 운영 환경에서는 비활성화
public class NotificationTestController {

	private final NotificationCommandService notificationCommandService;

	@Operation(summary = "테스트 알림 발송",
			description = "개발/테스트용 API. 본인에게 테스트 알림을 발송합니다.")
	@PostMapping("/send")
	public ApiResponse<String> sendTestNotification(
			@CurrentUser Member member,
			@RequestParam(defaultValue = "테스트 알림") String title,
			@RequestParam(defaultValue = "이것은 테스트 알림입니다.") String body
	) {
		try {
			notificationCommandService.createAndSendNotification(
					member,
					title,
					body,
					"/home",
					NotificationType.RECIPE_LIKE
			);
			return ApiResponse.onSuccess("테스트 알림이 발송되었습니다. 핸드폰을 확인하세요!");
		} catch (Exception e) {
			log.error("Test notification failed", e);
			return ApiResponse.onFailure("500", "알림 발송 실패: " + e.getMessage(), null);
		}
	}

	@Operation(summary = "레시피 좋아요 테스트 알림",
			description = "레시피 좋아요 알림을 테스트합니다.")
	@PostMapping("/recipe-like")
	public ApiResponse<String> sendRecipeLikeTest(@CurrentUser Member member) {
		notificationCommandService.createAndSendNotification(
				member,
				"레시피 좋아요",
				"테스트유저님이 회원님의 \"김치찌개\" 레시피를 좋아합니다!",
				"/recipe/123",
				NotificationType.RECIPE_LIKE
		);
		return ApiResponse.onSuccess("레시피 좋아요 알림 발송 완료");
	}

	@Operation(summary = "팔로우 테스트 알림",
			description = "팔로우 알림을 테스트합니다.")
	@PostMapping("/follow")
	public ApiResponse<String> sendFollowTest(@CurrentUser Member member) {
		notificationCommandService.createAndSendNotification(
				member,
				"새 팔로워",
				"테스트유저님이 회원님을 팔로우하기 시작했습니다.",
				"/profile/testuser",
				NotificationType.FOLLOW
		);
		return ApiResponse.onSuccess("팔로우 알림 발송 완료");
	}

	@Operation(summary = "유통기한 테스트 알림",
			description = "유통기한 알림을 테스트합니다.")
	@PostMapping("/expiration")
	public ApiResponse<String> sendExpirationTest(@CurrentUser Member member) {
		notificationCommandService.createAndSendNotification(
				member,
				"유통기한 알림",
				"우유의 유통기한이 3일 남았습니다!",
				"/refrigerator",
				NotificationType.EXPIRATION_ALERT
		);
		return ApiResponse.onSuccess("유통기한 알림 발송 완료");
	}

	@Operation(summary = "냉장고 초대 테스트 알림",
			description = "냉장고 초대 알림을 테스트합니다.")
	@PostMapping("/invitation")
	public ApiResponse<String> sendInvitationTest(@CurrentUser Member member) {
		notificationCommandService.createAndSendNotification(
				member,
				"냉장고 초대",
				"테스트유저님이 회원님을 공유 냉장고에 초대했습니다.",
				"/refrigerator/invitation/1",
				NotificationType.REFRIGERATOR_INVITATION
		);
		return ApiResponse.onSuccess("냉장고 초대 알림 발송 완료");
	}
}
