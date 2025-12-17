package novaminds.gradproj.domain.notification.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.notification.entity.Notification;
import novaminds.gradproj.domain.notification.entity.NotificationSettings;
import novaminds.gradproj.domain.notification.entity.NotificationType;
import novaminds.gradproj.domain.notification.repository.NotificationRepository;
import novaminds.gradproj.domain.notification.repository.NotificationSettingsRepository;
import novaminds.gradproj.domain.userdevice.entity.UserDevice;
import novaminds.gradproj.domain.userdevice.repository.UserDeviceRepository;
import novaminds.gradproj.global.push.service.ExpoPushNotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandService {

	private final NotificationRepository notificationRepository;
	private final NotificationSettingsRepository notificationSettingsRepository;
	private final UserDeviceRepository userDeviceRepository;
	private final ExpoPushNotificationService expoPushNotificationService;

	/**
	 * 알림 생성 및 발송
	 * @param member 알림을 받을 사용자
	 * @param title 알림 제목
	 * @param body 알림 내용
	 * @param deepLink 딥링크 URL
	 * @param type 알림 타입
	 */
	public Notification createAndSendNotification(
			Member member,
			String title,
			String body,
			String deepLink,
			NotificationType type
	) {
		// 1. 사용자 알림 설정 확인
		NotificationSettings settings = getNotificationSettings(member);

		// 2. 알림 타입별 설정 체크
		if (!shouldSendNotification(settings, type)) {
			log.info("User {} has disabled {} notifications", member.getLoginId(), type);
			return null;
		}

		// 3. DB에 알림 저장
		Notification notification = Notification.builder()
				.member(member)
				.title(title)
				.body(body)
				.deepLink(deepLink)
				.type(type)
				.isRead(false)
				.build();
		notificationRepository.save(notification);

		// 4. 푸시 알림 발송 (설정이 켜져있으면)
		if (settings.isEnablePush()) {
			sendPushToUserDevices(member, title, body, deepLink, type);
		}

		return notification;
	}

	/**
	 * 알림 타입에 따라 발송 여부 결정
	 */
	private boolean shouldSendNotification(NotificationSettings settings, NotificationType type) {
		return switch (type) {
			case EXPIRATION_ALERT -> settings.isEnableExpirationAlert();
			case RECIPE_LIKE -> settings.isEnableLikeNotification();
			case RECIPE_COMMENT, RECIPE_COMMENT_REPLY -> settings.isEnableCommentNotification();
			case REFRIGERATOR_INVITATION -> settings.isEnableRefrigeratorInvitation();
			case FOLLOW -> settings.isEnableFollow();
			case REFRIGERATOR_ITEM_ADDED -> settings.isEnableRefrigeratorItemAdded();
		};
	}

	/**
	 * 사용자의 모든 활성 디바이스에 푸시 발송
	 */
	private void sendPushToUserDevices(Member member, String title, String body, String deepLink, NotificationType type) {
		List<UserDevice> devices = userDeviceRepository.findByMemberAndIsActiveTrue(member);

		if (devices.isEmpty()) {
			log.warn("No active devices found for user: {}", member.getLoginId());
			return;
		}

		Map<String, Object> data = new HashMap<>();
		data.put("deepLink", deepLink);
		data.put("type", type.name());
		data.put("notificationType", type.name());

		for (UserDevice device : devices) {
			try {
				boolean success = expoPushNotificationService.sendPushNotification(
						device.getFcmToken(),
						title,
						body,
						data
				);
				if (success) {
					log.info("Push sent to device: {} for user: {}", device.getDeviceId(), member.getLoginId());
				}
			} catch (Exception e) {
				log.error("Failed to send push to device: {}", device.getDeviceId(), e);
			}
		}
	}

	/**
	 * 사용자 알림 설정 조회 (없으면 기본값으로 생성)
	 */
	private NotificationSettings getNotificationSettings(Member member) {
		return notificationSettingsRepository.findByMember(member)
				.orElseGet(() -> {
					NotificationSettings defaultSettings = NotificationSettings.builder()
							.member(member)
							.build();
					return notificationSettingsRepository.save(defaultSettings);
				});
	}

	/**
	 * 알림 읽음 처리
	 */
	public void markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
				.orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

		Notification updated = Notification.builder()
				.id(notification.getId())
				.member(notification.getMember())
				.title(notification.getTitle())
				.body(notification.getBody())
				.deepLink(notification.getDeepLink())
				.type(notification.getType())
				.isRead(true)
				.build();
		notificationRepository.save(updated);
	}

	/**
	 * 모든 알림 읽음 처리
	 */
	public void markAllAsRead(Member member) {
		List<Notification> unreadNotifications = notificationRepository.findByMemberAndIsReadFalse(member);
		unreadNotifications.forEach(notification -> {
			Notification updated = Notification.builder()
					.id(notification.getId())
					.member(notification.getMember())
					.title(notification.getTitle())
					.body(notification.getBody())
					.deepLink(notification.getDeepLink())
					.type(notification.getType())
					.isRead(true)
					.build();
			notificationRepository.save(updated);
		});
	}
}
