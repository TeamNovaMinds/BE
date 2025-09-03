package novaminds.gradproj.domain.recipe.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeCategory {

    KOREAN("한식"),       // 김치찌개, 불고기
    CHINESE("중식"),      // 짜장면, 마라탕
    JAPANESE("일식"),     // 초밥, 돈까스
    WESTERN("양식"),      // 파스타, 스테이크
    ASIAN("아시안"),      // 태국, 베트남 요리
    DESSERT("디저트"),    // 케이크, 푸딩
    BAKERY("베이커리"),   // 빵, 쿠키
    SNACK("간식"),        // 떡볶이, 튀김
    DRINK("음료/술")      // 칵테일, 전통주, 음료
    ;

    private final String koreanName;
}