package novaminds.gradproj.domain.userdevice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.user.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_devices",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"user_id", "device_id"})
		})
public class UserDevice extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(name = "device_type", nullable = false, length = 20)
	private DeviceType deviceType;

	@Column(name = "device_id", nullable = false, length = 100)
	private String deviceId;

	@Column(name = "fcm_token", nullable = false, columnDefinition = "TEXT")
	private String fcmToken;

	@Column(name = "is_active", nullable = false)
	@Builder.Default
	private boolean isActive = true;
}