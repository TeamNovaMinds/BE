package novaminds.gradproj.domain.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IngredientName {
    // 채소류
    ONION("양파"),
    GARLIC("마늘"),
    GREEN_ONION("파"),
    CARROT("당근"),
    POTATO("감자"),
    SWEET_POTATO("고구마"),
    CABBAGE("양배추"),
    CHINESE_CABBAGE("배추"),
    SPINACH("시금치"),
    LETTUCE("상추"),
    TOMATO("토마토"),
    CUCUMBER("오이"),
    PAPRIKA("파프리카"),
    MUSHROOM("버섯"),
    BEAN_SPROUTS("콩나물"),
    RADISH("무"),
    ZUCCHINI("애호박"),
    PUMPKIN("늙은호박"),
    EGGPLANT("가지"),
    CHILI_PEPPER("고추"),

    // 육류
    PORK_BELLY("삼겹살"),
    PORK_SHOULDER("목살"),
    PORK_LOIN("등심"),
    BEEF_SIRLOIN("소고기 등심"),
    BEEF_BRISKET("소고기 양지"),
    BEEF_SHORT_RIB("갈비"),
    CHICKEN_BREAST("닭가슴살"),
    CHICKEN_THIGH("닭다리"),
    CHICKEN_WING("닭날개"),

    // 해산물
    SHRIMP("새우"),
    SQUID("오징어"),
    OCTOPUS("문어"),
    MACKEREL("고등어"),
    SALMON("연어"),
    TUNA("참치"),
    ANCHOVY("멸치"),
    POLLOCK("명태"),

    // 계란/유제품
    EGG("계란"),
    MILK("우유"),
    CHEESE("치즈"),
    BUTTER("버터"),
    YOGURT("요거트"),
    CREAM("크림"),

    // 곡물/면류
    RICE("쌀"),
    FLOUR("밀가루"),
    BREAD("빵"),
    PASTA("파스타"),
    RAMEN("라면"),
    RICE_CAKE("떡"),

    // 양념/소스
    SOY_SAUCE("간장"),
    GOCHUJANG("고추장"),
    DOENJANG("된장"),
    SALT("소금"),
    SUGAR("설탕"),
    PEPPER("후추"),
    SESAME_OIL("참기름"),
    VEGETABLE_OIL("식용유"),
    VINEGAR("식초"),
    KETCHUP("케첩"),
    MAYONNAISE("마요네즈"),

    // 과일
    APPLE("사과"),
    BANANA("바나나"),
    ORANGE("오렌지"),
    GRAPE("포도"),
    STRAWBERRY("딸기"),
    WATERMELON("수박"),
    MELON("멜론"),
    PEACH("복숭아"),
    PEAR("배"),

    // 기타
    TOFU("두부"),
    KIMCHI("김치"),
    SEAWEED("김"),
    GREEN_TEA("녹차"),
    COFFEE("커피");

    private final String koreanName;
}