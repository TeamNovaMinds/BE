package novaminds.gradproj.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.ingredient.Ingredient;
import novaminds.gradproj.domain.ingredient.IngredientCategory;
import novaminds.gradproj.domain.refrigerator.Refrigerator;
import novaminds.gradproj.domain.Recipe.Recipe;
import novaminds.gradproj.domain.Recipe.RecipeLike;
import novaminds.gradproj.domain.Recipe.RecipeComment;
import novaminds.gradproj.domain.Notification.Notification;
import novaminds.gradproj.domain.userdevice.UserDevice;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@Column(name = "login_id", nullable = false, unique = true, length = 50)
	private String loginId;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true, length = 50)
	private String nickname;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "provider_id", length = 100)
	private String providerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", nullable = false, length = 20)
	private SocialType socialType;

	@Column(name = "is_profile_completed", nullable = false)
	@Builder.Default
	private boolean isProfileCompleted = false;

	@Column(nullable = false)
	@Builder.Default
	private Integer point = 0;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Refrigerator refrigerator;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Recipe> recipes = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecipeLike> recipeLikes = new ArrayList<>();

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecipeComment> recipeComments = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<UserDevice> devices = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<UserInterestCategory> interestCategories = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Ingredient> ingredientRegistration = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<IngredientCategory> categoryRegistration = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<UserRefrigeratorSkin> purchasedSkins = new ArrayList<>();

	@OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Follow> followers = new ArrayList<>();

	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Follow> followings = new ArrayList<>();

	public void increasePointByRecipeLike() {
		this.point += 10; // 좋아요당 10포인트
	}

	public void decreasePointByRecipeLikeCancel() {
		if (this.point >= 10) {
			this.point -= 10;
		}
	}

	public void usePoint(int amount) {
		if (this.point < amount) {
			throw new IllegalArgumentException("포인트가 부족합니다.");
		}
		this.point -= amount;
	}

	public void addPoint(int amount) {
		this.point += amount;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setRefrigerator(Refrigerator refrigerator) {
		this.refrigerator = refrigerator;
	}

	public void completeProfile() {
		this.isProfileCompleted = true;
	}

	public User updateOAuthInfo(String name, String picture) {
		this.name = name;
		this.profileImage = picture;
		return this;
	}

	public void updateProfileImage(String profileImageUrl) {
		this.profileImage = profileImageUrl;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}