package novaminds.gradproj.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.storeditem.StoredItem;
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

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<StoredItem> storedItems = new ArrayList<>();

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

	public void updateNickname(String nickname) {
		this.nickname = nickname;
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