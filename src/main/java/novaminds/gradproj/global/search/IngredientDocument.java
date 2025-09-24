package novaminds.gradproj.global.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "ingredients") // ingredients 라는 이름의 인덱스(서랍장)에 저장
@Setting(settingPath = "es-setting/nori-setting.json") // 한글 형태소 분석기 설정 추가
public class IngredientDocument {

    @Id
    private Long id; // 원본 DB의 Ingredient ID

    // 모든 검색을 책임질 단 하나의 메인 필드
    @Field(type = FieldType.Text, analyzer = "korean_unified_analyzer")
    private String ingredientName;

    // 정확 일치 및 정렬을 위한 키워드 필드
    @Field(type = FieldType.Keyword)
    private String ingredientNameKw;

    @Field(type = FieldType.Keyword, name = "category_name") // 카테고리 이름은 정확히 일치해야 하므로 Keyword 타입 사용
    private String categoryName;

    @Field(type = FieldType.Keyword, name = "image_url", index = false) // 이미지 URL은 검색 대상이 아니므로 index=false
    private String imageUrl;
}
