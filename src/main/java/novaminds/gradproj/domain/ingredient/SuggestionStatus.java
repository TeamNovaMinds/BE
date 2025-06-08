package novaminds.gradproj.domain.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuggestionStatus {

	PENDING("신청 대기"),
	APPROVED("승인"),
	REJECTED("거절");

	private final String description;
}
