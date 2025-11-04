package novaminds.gradproj.domain.ingredient.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IngredientCategory {
	VEGETABLE("채소"),
	FRUIT("과일"),
	MEAT("육류"),
	SEAFOOD("수산물"),
	DAIRY("유제품"),
	GRAIN("곡물"),
	SEASONING("조미료"),
	PROCESSED("가공식품");

	private final String displayName;
}