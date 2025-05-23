package novaminds.gradproj.domain.recipeingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.ingredient.Ingredient;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "alternative_ingredients")
public class AlternativeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_ingredient_id", nullable = false)
    private RecipeIngredient recipeIngredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient alternativeIngredient;

    //대체 시 주의사항
    @Column(length = 100)
    private String notes;

    //양방향 연관관계 편의 메서드
    protected void setRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    //정적 팩토리 메서드
    public static AlternativeIngredient create(RecipeIngredient recipeIngredient,
                                               Ingredient alternativeIngredient,
                                               String notes) {
        AlternativeIngredient alternative = AlternativeIngredient.builder()
                .recipeIngredient(recipeIngredient)
                .alternativeIngredient(alternativeIngredient)
                .notes(notes)
                .build();

        // 양방향 관계 설정
        recipeIngredient.getAlternatives().add(alternative);

        return alternative;
    }
}