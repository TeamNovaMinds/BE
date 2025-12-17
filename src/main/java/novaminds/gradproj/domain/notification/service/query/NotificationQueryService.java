package novaminds.gradproj.domain.notification.service.query;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.notification.converter.NotificationConverter;
import novaminds.gradproj.domain.notification.entity.Notification;
import novaminds.gradproj.domain.notification.repository.NotificationRepository;
import novaminds.gradproj.domain.notification.web.dto.NotificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

	private final NotificationRepository notificationRepository;

	/**
	 * 사용자의 알림 목록 조회 (페이징)
	 */
	public NotificationResponseDTO.NotificationListResponse getNotifications(Member member, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Notification> notificationPage = notificationRepository.findByMemberOrderByCreatedAtDesc(member, pageable);

		List<NotificationResponseDTO.NotificationInfo> notificationList = notificationPage.getContent().stream()
				.map(NotificationConverter::toNotificationInfo)
				.collect(Collectors.toList());

		long unreadCount = notificationRepository.countByMemberAndIsReadFalse(member);

		return NotificationResponseDTO.NotificationListResponse.builder()
				.notifications(notificationList)
				.unreadCount(unreadCount)
				.currentPage(notificationPage.getNumber())
				.totalPages(notificationPage.getTotalPages())
				.totalElements(notificationPage.getTotalElements())
				.build();
	}

	/**
	 * 읽지 않은 알림 개수 조회
	 */
	public long getUnreadCount(Member member) {
		return notificationRepository.countByMemberAndIsReadFalse(member);
	}
}
