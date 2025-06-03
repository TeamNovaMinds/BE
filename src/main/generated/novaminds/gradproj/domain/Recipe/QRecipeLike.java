package novaminds.gradproj.domain.Recipe;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeLike is a Querydsl query type for RecipeLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeLike extends EntityPathBase<RecipeLike> {

    private static final long serialVersionUID = 1427576805L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeLike recipeLike = new QRecipeLike("recipeLike");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRecipe recipe;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final novaminds.gradproj.domain.user.QUser user;

    public QRecipeLike(String variable) {
        this(RecipeLike.class, forVariable(variable), INITS);
    }

    public QRecipeLike(Path<? extends RecipeLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeLike(PathMetadata metadata, PathInits inits) {
        this(RecipeLike.class, metadata, inits);
    }

    public QRecipeLike(Class<? extends RecipeLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipe = inits.isInitialized("recipe") ? new QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
        this.user = inits.isInitialized("user") ? new novaminds.gradproj.domain.user.QUser(forProperty("user")) : null;
    }

}

