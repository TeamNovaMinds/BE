package novaminds.gradproj.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.notification.entity.NotificationSettings;
import novaminds.gradproj.domain.notification.entity.NotificationType;
import novaminds.gradproj.domain.notification.repository.NotificationSettingsRepository;
import novaminds.gradproj.domain.notification.service.command.NotificationCommandService;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.repository.StoredItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpirationNotificationScheduler {

	private final StoredItemRepository storedItemRepository;
	private final NotificationCommandService notificationCommandService;
	private final NotificationSettingsRepository notificationSettingsRepository;

	/**
	 * 매일 오전 9시에 유통기한 임박 식재료 알림 발송
	 * cron: 초 분 시 일 월 요일
	 */
	@Scheduled(cron = "0 0 9 * * *")
	public void checkExpiringItems() {
		log.info("Starting expiration notification check at {}", LocalDate.now());

		try {
			// 모든 사용자의 유통기한 임박 아이템을 찾아서 알림 발송
			Map<Member, List<StoredItem>> expiringItemsByMember = new HashMap<>();

			// 향후 7일 이내 만료되는 아이템 조회
			LocalDate checkDate = LocalDate.now().plusDays(7);
			List<StoredItem> expiringItems = storedItemRepository.findItemsExpiringBefore(checkDate);

			log.info("Found {} items expiring before {}", expiringItems.size(), checkDate);

			// 사용자별로 그룹화
			for (StoredItem item : expiringItems) {
				for (Member member : item.getRefrigerator().getMemberList()) {
					// 사용자의 알림 설정 확인
					NotificationSettings settings = notificationSettingsRepository.findByMember(member)
							.orElse(null);

					if (settings == null || !settings.isEnableExpirationAlert()) {
						continue;
					}

					// 설정된 일수 이내인지 확인
					long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), item.getExpirationDate());
					if (daysUntilExpiration <= settings.getExpirationAlertDays() && daysUntilExpiration >= 0) {
						expiringItemsByMember.computeIfAbsent(member, k -> new java.util.ArrayList<>()).add(item);
					}
				}
			}

			// 각 사용자에게 알림 발송
			for (Map.Entry<Member, List<StoredItem>> entry : expiringItemsByMember.entrySet()) {
				Member member = entry.getKey();
				List<StoredItem> items = entry.getValue();

				sendExpirationNotification(member, items);
			}

			log.info("Expiration notification check completed. Sent to {} users", expiringItemsByMember.size());

		} catch (Exception e) {
			log.error("Error during expiration notification check", e);
		}
	}

	/**
	 * 유통기한 알림 발송
	 */
	private void sendExpirationNotification(Member member, List<StoredItem> items) {
		if (items.isEmpty()) {
			return;
		}

		try {
			// 첫 번째 아이템 정보로 알림 생성 (여러 개면 개수 포함)
			StoredItem firstItem = items.get(0);
			Ingredient ingredient = firstItem.getIngredient();
			long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), firstItem.getExpirationDate());

			String title;
			String body;
			String deepLink = "/refrigerator"; // 냉장고 화면으로 이동

			if (items.size() == 1) {
				title = "유통기한 알림";
				if (daysUntilExpiration == 0) {
					body = String.format("%s의 유통기한이 오늘까지입니다!", ingredient.getIngredientName());
				} else if (daysUntilExpiration == 1) {
					body = String.format("%s의 유통기한이 내일까지입니다!", ingredient.getIngredientName());
				} else {
					body = String.format("%s의 유통기한이 %d일 남았습니다!", ingredient.getIngredientName(), daysUntilExpiration);
				}
			} else {
				title = "유통기한 알림";
				body = String.format("%s 외 %d개 식재료의 유통기한이 임박했습니다!", ingredient.getIngredientName(), items.size() - 1);
			}

			notificationCommandService.createAndSendNotification(
					member,
					title,
					body,
					deepLink,
					NotificationType.EXPIRATION_ALERT
			);

			log.info("Sent expiration notification to user: {}, items: {}", member.getLoginId(), items.size());

		} catch (Exception e) {
			log.error("Failed to send expiration notification to user: {}", member.getLoginId(), e);
		}
	}
}
