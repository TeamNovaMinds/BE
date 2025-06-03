package novaminds.gradproj.domain.recipeingredient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlternativeIngredient is a Querydsl query type for AlternativeIngredient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlternativeIngredient extends EntityPathBase<AlternativeIngredient> {

    private static final long serialVersionUID = -1176045265L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlternativeIngredient alternativeIngredient1 = new QAlternativeIngredient("alternativeIngredient1");

    public final novaminds.gradproj.domain.ingredient.QIngredient alternativeIngredient;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath notes = createString("notes");

    public final QRecipeIngredient recipeIngredient;

    public QAlternativeIngredient(String variable) {
        this(AlternativeIngredient.class, forVariable(variable), INITS);
    }

    public QAlternativeIngredient(Path<? extends AlternativeIngredient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlternativeIngredient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlternativeIngredient(PathMetadata metadata, PathInits inits) {
        this(AlternativeIngredient.class, metadata, inits);
    }

    public QAlternativeIngredient(Class<? extends AlternativeIngredient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alternativeIngredient = inits.isInitialized("alternativeIngredient") ? new novaminds.gradproj.domain.ingredient.QIngredient(forProperty("alternativeIngredient"), inits.get("alternativeIngredient")) : null;
        this.recipeIngredient = inits.isInitialized("recipeIngredient") ? new QRecipeIngredient(forProperty("recipeIngredient"), inits.get("recipeIngredient")) : null;
    }

}

