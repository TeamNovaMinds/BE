package novaminds.gradproj.domain.recipeingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.Recipe;
import novaminds.gradproj.domain.ingredient.Ingredient;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipe_ingredients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"recipe_id", "ingredient_order"})
        }) //TODO : 레시피당 순서는 고유해야 하나...??
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

    //TODO : 대체 가능한 재료가 있는지 여부 -> 대체 가능한 재료는 어떻게 표시하지???
    @Column(nullable = false)
    @Builder.Default
    private boolean isRequired = true;
}