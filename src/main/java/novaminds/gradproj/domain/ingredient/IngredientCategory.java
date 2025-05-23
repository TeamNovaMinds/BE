package novaminds.gradproj.domain.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IngredientCategory {
    VEGETABLE("채소"),
    MEAT("육류"),
    SEAFOOD("해산물"),
    DAIRY("유제품"),
    GRAIN("곡물"),
    SEASONING("양념/소스"),
    FRUIT("과일"),
    PROCESSED("가공식품"),
    BEVERAGE("음료"),
    OTHER("기타");

    private final String koreanName;
}