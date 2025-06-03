package novaminds.gradproj.domain.recipeingredient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeIngredient is a Querydsl query type for RecipeIngredient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeIngredient extends EntityPathBase<RecipeIngredient> {

    private static final long serialVersionUID = -46091378L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeIngredient recipeIngredient = new QRecipeIngredient("recipeIngredient");

    public final ListPath<AlternativeIngredient, QAlternativeIngredient> alternatives = this.<AlternativeIngredient, QAlternativeIngredient>createList("alternatives", AlternativeIngredient.class, QAlternativeIngredient.class, PathInits.DIRECT2);

    public final StringPath amount = createString("amount");

    public final StringPath description = createString("description");

    public final BooleanPath hasAlternatives = createBoolean("hasAlternatives");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final novaminds.gradproj.domain.ingredient.QIngredient ingredient;

    public final NumberPath<Integer> ingredientOrder = createNumber("ingredientOrder", Integer.class);

    public final BooleanPath isRequired = createBoolean("isRequired");

    public final novaminds.gradproj.domain.Recipe.QRecipe recipe;

    public final StringPath recipeIngredientImgUrl = createString("recipeIngredientImgUrl");

    public QRecipeIngredient(String variable) {
        this(RecipeIngredient.class, forVariable(variable), INITS);
    }

    public QRecipeIngredient(Path<? extends RecipeIngredient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeIngredient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeIngredient(PathMetadata metadata, PathInits inits) {
        this(RecipeIngredient.class, metadata, inits);
    }

    public QRecipeIngredient(Class<? extends RecipeIngredient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ingredient = inits.isInitialized("ingredient") ? new novaminds.gradproj.domain.ingredient.QIngredient(forProperty("ingredient"), inits.get("ingredient")) : null;
        this.recipe = inits.isInitialized("recipe") ? new novaminds.gradproj.domain.Recipe.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

