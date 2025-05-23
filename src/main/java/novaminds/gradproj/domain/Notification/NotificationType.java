package novaminds.gradproj.domain.Notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

	EXPIRATION_ALERT("유통기한 알림"),
//	RECIPE_RECOMMENDATION("레시피 추천"),
	RECIPE_LIKE("레시피 좋아요"),
	RECIPE_COMMENT("레시피 댓글"),
	RECIPE_COMMENT_REPLY("댓글 답글");

	private final String description;
}