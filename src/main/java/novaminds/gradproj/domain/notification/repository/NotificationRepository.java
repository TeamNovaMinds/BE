package novaminds.gradproj.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
