package novaminds.gradproj.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.notification.entity.NotificationSettings;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
}
