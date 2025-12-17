package novaminds.gradproj.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

	EXPIRATION_ALERT("유통기한 알림"),
//	RECIPE_RECOMMENDATION("레시피 추천"),
	RECIPE_LIKE("레시피 좋아요"),
	RECIPE_COMMENT("레시피 댓글"),
	RECIPE_COMMENT_REPLY("댓글 답글"),
	REFRIGERATOR_INVITATION("냉장고 초대"),
	FOLLOW("팔로우 알림"),
	REFRIGERATOR_ITEM_ADDED("냉장고 재료 추가");

	private final String description;
}