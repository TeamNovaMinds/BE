package novaminds.gradproj.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserInterestCategory is a Querydsl query type for UserInterestCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserInterestCategory extends EntityPathBase<UserInterestCategory> {

    private static final long serialVersionUID = 768135126L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserInterestCategory userInterestCategory = new QUserInterestCategory("userInterestCategory");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    public final EnumPath<novaminds.gradproj.domain.Recipe.RecipeCategory> category = createEnum("category", novaminds.gradproj.domain.Recipe.RecipeCategory.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserInterestCategory(String variable) {
        this(UserInterestCategory.class, forVariable(variable), INITS);
    }

    public QUserInterestCategory(Path<? extends UserInterestCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserInterestCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserInterestCategory(PathMetadata metadata, PathInits inits) {
        this(UserInterestCategory.class, metadata, inits);
    }

    public QUserInterestCategory(Class<? extends UserInterestCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

