package novaminds.gradproj.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import novaminds.gradproj.domain.refrigerator.entity.MemberRefrigeratorSkin;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.entity.IngredientCategory;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeLike;
import novaminds.gradproj.domain.recipe.entity.RecipeComment;
import novaminds.gradproj.domain.notification.entity.Notification;
import novaminds.gradproj.domain.userdevice.entity.UserDevice;
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
@Table(name = "member")
public class Member extends BaseEntity {

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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private Refrigerator refrigerator;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Recipe> recipes = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecipeLike> recipeLikes = new ArrayList<>();

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<RecipeComment> recipeComments = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<UserDevice> devices = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<MemberInterestCategory> interestCategories = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Ingredient> ingredientRegistration = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<IngredientCategory> categoryRegistration = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<MemberRefrigeratorSkin> purchasedSkins = new ArrayList<>();

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

    public void completeProfile() {
		this.isProfileCompleted = true;
	}

	public Member updateOAuthInfo(String name, String picture) {
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

	public void setRefrigerator(Refrigerator refrigerator) {
		// 기존 냉장고가 있고 새 냉장고와 다르면 기존 냉장고의 member 참조 해제
		if (this.refrigerator != null && this.refrigerator != refrigerator) {
			this.refrigerator.setMember(null);
		}
		
		// 새 냉장고 할당
		this.refrigerator = refrigerator;
		
		// 새 냉장고가 null이 아니면 양방향 연관관계 설정
		if (refrigerator != null) {
			refrigerator.setMember(this);
		}
	}
}