package novaminds.gradproj.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -486987410L;

    public static final QUser user = new QUser("user");

    public final novaminds.gradproj.domain.QBaseEntity _super = new novaminds.gradproj.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<novaminds.gradproj.domain.userdevice.UserDevice, novaminds.gradproj.domain.userdevice.QUserDevice> devices = this.<novaminds.gradproj.domain.userdevice.UserDevice, novaminds.gradproj.domain.userdevice.QUserDevice>createList("devices", novaminds.gradproj.domain.userdevice.UserDevice.class, novaminds.gradproj.domain.userdevice.QUserDevice.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<UserInterestCategory, QUserInterestCategory> interestCategories = this.<UserInterestCategory, QUserInterestCategory>createList("interestCategories", UserInterestCategory.class, QUserInterestCategory.class, PathInits.DIRECT2);

    public final BooleanPath isProfileCompleted = createBoolean("isProfileCompleted");

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final ListPath<novaminds.gradproj.domain.Notification.Notification, novaminds.gradproj.domain.Notification.QNotification> notifications = this.<novaminds.gradproj.domain.Notification.Notification, novaminds.gradproj.domain.Notification.QNotification>createList("notifications", novaminds.gradproj.domain.Notification.Notification.class, novaminds.gradproj.domain.Notification.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath providerId = createString("providerId");

    public final ListPath<novaminds.gradproj.domain.Recipe.RecipeComment, novaminds.gradproj.domain.Recipe.QRecipeComment> recipeComments = this.<novaminds.gradproj.domain.Recipe.RecipeComment, novaminds.gradproj.domain.Recipe.QRecipeComment>createList("recipeComments", novaminds.gradproj.domain.Recipe.RecipeComment.class, novaminds.gradproj.domain.Recipe.QRecipeComment.class, PathInits.DIRECT2);

    public final ListPath<novaminds.gradproj.domain.Recipe.RecipeLike, novaminds.gradproj.domain.Recipe.QRecipeLike> recipeLikes = this.<novaminds.gradproj.domain.Recipe.RecipeLike, novaminds.gradproj.domain.Recipe.QRecipeLike>createList("recipeLikes", novaminds.gradproj.domain.Recipe.RecipeLike.class, novaminds.gradproj.domain.Recipe.QRecipeLike.class, PathInits.DIRECT2);

    public final ListPath<novaminds.gradproj.domain.Recipe.Recipe, novaminds.gradproj.domain.Recipe.QRecipe> recipes = this.<novaminds.gradproj.domain.Recipe.Recipe, novaminds.gradproj.domain.Recipe.QRecipe>createList("recipes", novaminds.gradproj.domain.Recipe.Recipe.class, novaminds.gradproj.domain.Recipe.QRecipe.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public final ListPath<novaminds.gradproj.domain.storeditem.StoredItem, novaminds.gradproj.domain.storeditem.QStoredItem> storedItems = this.<novaminds.gradproj.domain.storeditem.StoredItem, novaminds.gradproj.domain.storeditem.QStoredItem>createList("storedItems", novaminds.gradproj.domain.storeditem.StoredItem.class, novaminds.gradproj.domain.storeditem.QStoredItem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

