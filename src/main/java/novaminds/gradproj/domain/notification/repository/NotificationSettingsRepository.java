package novaminds.gradproj.domain.notification.repository;

import novaminds.gradproj.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.notification.entity.NotificationSettings;

import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

	Optional<NotificationSettings> findByMember(Member member);
}
