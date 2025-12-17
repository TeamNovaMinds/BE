package novaminds.gradproj.domain.notification.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDTO {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class NotificationInfo {
		private Long id;
		private String title;
		private String body;
		private String deepLink;
		private NotificationType type;
		private boolean isRead;
		private LocalDateTime createdAt;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class NotificationListResponse {
		private List<NotificationInfo> notifications;
		private long unreadCount;
		private int currentPage;
		private int totalPages;
		private long totalElements;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UnreadCountResponse {
		private long unreadCount;
	}
}
