package novaminds.gradproj.domain.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuggestionStatus {

	Pending("신청 대기"),
	Approved("승인"),
	Rejected("거절");

	private final String description;
}
