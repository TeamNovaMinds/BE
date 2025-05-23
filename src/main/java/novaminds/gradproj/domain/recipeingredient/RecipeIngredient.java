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
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    private int ingredientOrder;

    @Column(columnDefinition = "TEXT")
    private String Description;

    private String recipeIngredientImgUrl;
}
