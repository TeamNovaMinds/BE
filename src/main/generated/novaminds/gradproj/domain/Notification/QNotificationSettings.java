package novaminds.gradproj.domain.Notification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificationSettings is a Querydsl query type for NotificationSettings
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationSettings extends EntityPathBase<NotificationSettings> {

    private static final long serialVersionUID = -80135023L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificationSettings notificationSettings = new QNotificationSettings("notificationSettings");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath enableCommentNotification = createBoolean("enableCommentNotification");

    public final BooleanPath enableEmail = createBoolean("enableEmail");

    public final BooleanPath enableExpirationAlert = createBoolean("enableExpirationAlert");

    public final BooleanPath enableLikeNotification = createBoolean("enableLikeNotification");

    public final BooleanPath enablePush = createBoolean("enablePush");

    public final NumberPath<Integer> expirationAlertDays = createNumber("expirationAlertDays", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final novaminds.gradproj.domain.user.QUser user;

    public QNotificationSettings(String variable) {
        this(NotificationSettings.class, forVariable(variable), INITS);
    }

    public QNotificationSettings(Path<? extends NotificationSettings> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificationSettings(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificationSettings(PathMetadata metadata, PathInits inits) {
        this(NotificationSettings.class, metadata, inits);
    }

    public QNotificationSettings(Class<? extends NotificationSettings> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new novaminds.gradproj.domain.user.QUser(forProperty("user")) : null;
    }

}

