package novaminds.gradproj.domain.Recipe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeCategory {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    WESTERN("양식"),
    ASIAN("아시안"),
    DESSERT("디저트"),
    SALAD("샐러드"),
    SOUP("국/찌개"),
    SIDE_DISH("반찬"),
    NOODLE("면요리"),
    RICE("밥요리"),
    BREAD("빵/베이커리"),
    DRINK("음료"),
    SNACK("간식"),
    DIET("다이어트"),
    VEGAN("비건"),
    QUICK("간편요리"),
    PARTY("파티요리"),
    KIDS("아이간식"),
    OTHER("기타");

    //TODO : 편의점 음식, 술 종류 추가 필요

    private final String koreanName;
}
