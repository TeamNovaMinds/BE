package novaminds.gradproj.domain.storeditem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoredItem is a Querydsl query type for StoredItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoredItem extends EntityPathBase<StoredItem> {

    private static final long serialVersionUID = 1668032110L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoredItem storedItem = new QStoredItem("storedItem");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> expirationDate = createDate("expirationDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final novaminds.gradproj.domain.ingredient.QIngredient ingredient;

    public final novaminds.gradproj.domain.user.QUser owner;

    public final EnumPath<StorageType> storageType = createEnum("storageType", StorageType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoredItem(String variable) {
        this(StoredItem.class, forVariable(variable), INITS);
    }

    public QStoredItem(Path<? extends StoredItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoredItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoredItem(PathMetadata metadata, PathInits inits) {
        this(StoredItem.class, metadata, inits);
    }

    public QStoredItem(Class<? extends StoredItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ingredient = inits.isInitialized("ingredient") ? new novaminds.gradproj.domain.ingredient.QIngredient(forProperty("ingredient"), inits.get("ingredient")) : null;
        this.owner = inits.isInitialized("owner") ? new novaminds.gradproj.domain.user.QUser(forProperty("owner")) : null;
    }

}

