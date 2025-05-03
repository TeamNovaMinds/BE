package novaminds.gradproj.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	private String loginId;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	private String profileImage;

	// 사용자와 캘린더는 1:1 관계
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Calendar calendar;

	// 사용자가 보낸 친구 요청
	@OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
	private List<Friendship> sentFriendRequests = new ArrayList<>();

	// 사용자가 받은 친구 요청
	@OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
	private List<Friendship> receivedFriendRequests = new ArrayList<>();

	// 사용자가 생성한 약속 방
	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
	private List<MeetingRoom> createdRooms = new ArrayList<>();

	// 사용자가 참여한 약속 방
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<RoomParticipant> participatingRooms = new ArrayList<>();

	// FCM 알림 지원을 위한 추가 필드
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserDevice> devices = new ArrayList<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private NotificationSettings notificationSettings;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();
}
