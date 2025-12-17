package novaminds.gradproj.domain.notification.repository;

import novaminds.gradproj.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	Page<Notification> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

	List<Notification> findByMemberAndIsReadFalse(Member member);

	long countByMemberAndIsReadFalse(Member member);
}
