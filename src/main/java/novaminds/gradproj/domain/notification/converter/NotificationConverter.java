package novaminds.gradproj.domain.notification.converter;

import novaminds.gradproj.domain.notification.entity.Notification;
import novaminds.gradproj.domain.notification.web.dto.NotificationResponseDTO;

public class NotificationConverter {

	public static NotificationResponseDTO.NotificationInfo toNotificationInfo(Notification notification) {
		return NotificationResponseDTO.NotificationInfo.builder()
				.id(notification.getId())
				.title(notification.getTitle())
				.body(notification.getBody())
				.deepLink(notification.getDeepLink())
				.type(notification.getType())
				.isRead(notification.isRead())
				.createdAt(notification.getCreatedAt())
				.build();
	}
}
