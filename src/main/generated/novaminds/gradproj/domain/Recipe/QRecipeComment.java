package novaminds.gradproj.domain.Recipe;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeComment is a Querydsl query type for RecipeComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeComment extends EntityPathBase<RecipeComment> {

    private static final long serialVersionUID = 950795089L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeComment recipeComment = new QRecipeComment("recipeComment");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    public final novaminds.gradproj.domain.user.QUser author;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecipeComment parentComment;

    public final QRecipe recipe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRecipeComment(String variable) {
        this(RecipeComment.class, forVariable(variable), INITS);
    }

    public QRecipeComment(Path<? extends RecipeComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeComment(PathMetadata metadata, PathInits inits) {
        this(RecipeComment.class, metadata, inits);
    }

    public QRecipeComment(Class<? extends RecipeComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new novaminds.gradproj.domain.user.QUser(forProperty("author")) : null;
        this.parentComment = inits.isInitialized("parentComment") ? new QRecipeComment(forProperty("parentComment"), inits.get("parentComment")) : null;
        this.recipe = inits.isInitialized("recipe") ? new QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

