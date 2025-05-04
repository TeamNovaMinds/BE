package novaminds.gradproj.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import novaminds.gradproj.domain.common.BaseEntity;

@Entity
public class NotificationSettings extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "login_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private boolean eventReminder = true;

	@Column(nullable = false)
	private boolean friendRequest = true;

	@Column(nullable = false)
	private boolean meetingRoomInvitation = true;

	@Column(nullable = false)
	private boolean meetingTimeSuggestion = true;
}

