package novaminds.gradproj.domain.ingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.storeditem.StoredItem;
import novaminds.gradproj.domain.recipeingredient.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private IngredientName ingredientName;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private IngredientCategory ingredientCategory;

    @Embedded
    private ShelfLife shelfLife;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoredItem> storedItems = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}