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

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "ingredient_name", analyzer = "korean_nori"),
            otherFields = {
                    @InnerField(suffix = "auto", type = FieldType.Text, analyzer = "korean_autocomplete"),
                    @InnerField(suffix = "kw", type = FieldType.Keyword)
            }
    )
    private String ingredientName;

    @Field(type = FieldType.Keyword, name = "category_name") // 카테고리 이름은 정확히 일치해야 하므로 Keyword 타입 사용
    private String categoryName;

    @Field(type = FieldType.Keyword, name = "image_url", index = false) // 이미지 URL은 검색 대상이 아니므로 index=false
    private String imageUrl;
}
