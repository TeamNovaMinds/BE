package novaminds.gradproj.domain.ingredient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShelfLife is a Querydsl query type for ShelfLife
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QShelfLife extends BeanPath<ShelfLife> {

    private static final long serialVersionUID = 714298121L;

    public static final QShelfLife shelfLife = new QShelfLife("shelfLife");

    public final NumberPath<Integer> freezerDays = createNumber("freezerDays", Integer.class);

    public final NumberPath<Integer> fridgeDays = createNumber("fridgeDays", Integer.class);

    public final NumberPath<Integer> roomTempDays = createNumber("roomTempDays", Integer.class);

    public QShelfLife(String variable) {
        super(ShelfLife.class, forVariable(variable));
    }

    public QShelfLife(Path<? extends ShelfLife> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShelfLife(PathMetadata metadata) {
        super(ShelfLife.class, metadata);
    }

}

