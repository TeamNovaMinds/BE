package novaminds.gradproj.domain.ingredient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIngredient is a Querydsl query type for Ingredient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredient extends EntityPathBase<Ingredient> {

    private static final long serialVersionUID = 592991086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIngredient ingredient = new QIngredient("ingredient");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<IngredientCategory> ingredientCategory = createEnum("ingredientCategory", IngredientCategory.class);

    public final EnumPath<IngredientName> ingredientName = createEnum("ingredientName", IngredientName.class);

    public final ListPath<novaminds.gradproj.domain.recipeingredient.RecipeIngredient, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient> recipeIngredients = this.<novaminds.gradproj.domain.recipeingredient.RecipeIngredient, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient>createList("recipeIngredients", novaminds.gradproj.domain.recipeingredient.RecipeIngredient.class, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient.class, PathInits.DIRECT2);

    public final QShelfLife shelfLife;

    public final ListPath<novaminds.gradproj.domain.storeditem.StoredItem, novaminds.gradproj.domain.storeditem.QStoredItem> storedItems = this.<novaminds.gradproj.domain.storeditem.StoredItem, novaminds.gradproj.domain.storeditem.QStoredItem>createList("storedItems", novaminds.gradproj.domain.storeditem.StoredItem.class, novaminds.gradproj.domain.storeditem.QStoredItem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QIngredient(String variable) {
        this(Ingredient.class, forVariable(variable), INITS);
    }

    public QIngredient(Path<? extends Ingredient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIngredient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIngredient(PathMetadata metadata, PathInits inits) {
        this(Ingredient.class, metadata, inits);
    }

    public QIngredient(Class<? extends Ingredient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.shelfLife = inits.isInitialized("shelfLife") ? new QShelfLife(forProperty("shelfLife")) : null;
    }

}

