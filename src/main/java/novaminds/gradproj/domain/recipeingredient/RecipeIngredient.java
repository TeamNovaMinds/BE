package novaminds.gradproj.domain.recipeingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.Recipe;
import novaminds.gradproj.domain.ingredient.Ingredient;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "ingredient_order", nullable = false)
    private int ingredientOrder;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "recipe_ingredient_img_url")
    private String recipeIngredientImgUrl;

    @Column(length = 50)
    private String amount;

    //필수 재료 여부
    @Column(nullable = false)
    @Builder.Default
    private boolean isRequired = true;

    //대체 재료 존재 여부
    @Column(name = "has_alternatives", nullable = false)
    @Builder.Default
    private boolean hasAlternatives = false;

    //hasAlternatives가 true일 때만 사용
    @OneToMany(mappedBy = "recipeIngredient", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AlternativeIngredient> alternatives = new ArrayList<>();

    //대체 재료 추가 메서드, 근데 왠만하면 애 사용하는 것보다는 AlternativeIngredient의 create() (정적 팩토리 메서드) 사용 권장
    public void addAlternative(AlternativeIngredient alternative) {
        this.alternatives.add(alternative);
        alternative.setRecipeIngredient(this); // protected 메서드 사용
    }

    //대체 재료 제거 메서드
    public void removeAlternative(AlternativeIngredient alternative) {
        this.alternatives.remove(alternative);
        alternative.setRecipeIngredient(null); // protected 메서드 사용
    }

    //대체 재료 존재 여부 확인 메서드
    public boolean hasAlternatives() {
        return !this.alternatives.isEmpty();
    }

    //모든 대체 재료 제거 메서드
    public void clearAlternatives() {
        for (AlternativeIngredient alternative : new ArrayList<>(this.alternatives)) {
            removeAlternative(alternative);
        }
    }
}