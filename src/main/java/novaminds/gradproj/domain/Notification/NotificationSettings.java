package novaminds.gradproj.domain.Notification;

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
@Table(name = "notification_settings")
public class NotificationSettings extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(nullable = false)
	@Builder.Default
	private boolean enablePush = true;

	@Column(nullable = false)
	@Builder.Default
	private boolean enableEmail = false;

	//유통기한 알림 설정
	@Column(nullable = false)
	@Builder.Default
	private boolean enableExpirationAlert = true;

	//유통기한 며칠 전에 알림 받을지
	@Column(nullable = false)
	@Builder.Default
	private int expirationAlertDays = 3;

/*
	//레시피 추천 알림
	@Column(nullable = false)
	@Builder.Default
	private boolean enableRecipeRecommendation = true;
*/

	//댓글 알림
	@Column(nullable = false)
	@Builder.Default
	private boolean enableCommentNotification = true;

	//좋아요 알림
	@Column(nullable = false)
	@Builder.Default
	private boolean enableLikeNotification = true;

	//TODO : 랭킹 알림은 일단 좀 더 고민해봐야 할 듯
}