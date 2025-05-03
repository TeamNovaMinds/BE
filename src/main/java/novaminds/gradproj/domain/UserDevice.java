package novaminds.gradproj.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import novaminds.gradproj.domain.common.BaseEntity;
import novaminds.gradproj.domain.enums.DeviceType;

@Entity
public class UserDevice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DeviceType deviceType;

	@Column(nullable = false)
	private String deviceId;

	@Column(nullable = false)
	private String fcmToken;

	@Column(nullable = false)
	private boolean isActive = true;
}
