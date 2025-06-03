package novaminds.gradproj.domain.Recipe;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipe is a Querydsl query type for Recipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipe extends EntityPathBase<Recipe> {

    private static final long serialVersionUID = -1828701394L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipe recipe = new QRecipe("recipe");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    public final novaminds.gradproj.domain.user.QUser author;

    public final NumberPath<Integer> cookingTimeMinutes = createNumber("cookingTimeMinutes", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final EnumPath<Difficulty> difficulty = createEnum("difficulty", Difficulty.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final EnumPath<RecipeCategory> recipeCategory = createEnum("recipeCategory", RecipeCategory.class);

    public final ListPath<RecipeComment, QRecipeComment> recipeComments = this.<RecipeComment, QRecipeComment>createList("recipeComments", RecipeComment.class, QRecipeComment.class, PathInits.DIRECT2);

    public final StringPath recipeImgUrl = createString("recipeImgUrl");

    public final ListPath<novaminds.gradproj.domain.recipeingredient.RecipeIngredient, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient> recipeIngredients = this.<novaminds.gradproj.domain.recipeingredient.RecipeIngredient, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient>createList("recipeIngredients", novaminds.gradproj.domain.recipeingredient.RecipeIngredient.class, novaminds.gradproj.domain.recipeingredient.QRecipeIngredient.class, PathInits.DIRECT2);

    public final ListPath<RecipeLike, QRecipeLike> recipeLikes = this.<RecipeLike, QRecipeLike>createList("recipeLikes", RecipeLike.class, QRecipeLike.class, PathInits.DIRECT2);

    public final NumberPath<Integer> servings = createNumber("servings", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecipe(String variable) {
        this(Recipe.class, forVariable(variable), INITS);
    }

    public QRecipe(Path<? extends Recipe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipe(PathMetadata metadata, PathInits inits) {
        this(Recipe.class, metadata, inits);
    }

    public QRecipe(Class<? extends Recipe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new novaminds.gradproj.domain.user.QUser(forProperty("author")) : null;
    }

}

