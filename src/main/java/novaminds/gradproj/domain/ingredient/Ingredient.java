package novaminds.gradproj.domain.ingredient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.Recipe.Difficulty;
import novaminds.gradproj.domain.storeditem.StoredItem;
import novaminds.gradproj.domain.recipeingredient.RecipeIngredient;
import novaminds.gradproj.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

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
    private String ingredientName;

    @Embedded
    private ShelfLife shelfLife;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @ColumnDefault("'Approved")
    private SuggestionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredientCategory_id", nullable = false)
    private IngredientCategory ingredientCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoredItem> storedItems = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}