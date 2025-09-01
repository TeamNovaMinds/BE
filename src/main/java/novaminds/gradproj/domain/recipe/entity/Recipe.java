package novaminds.gradproj.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.global.BaseEntity;
import novaminds.gradproj.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer likes = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RecipeCategory recipeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(name = "cooking_time_minutes")
    private Integer cookingTimeMinutes; //TODO : 시간을 10 ~ 15분 이런식으로 표현하려면 나중에 수정 필요

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Difficulty difficulty;

    @Column
    private Integer servings; //몇 인분인지 나타내는 필드

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("imageOrder ASC")
    @Builder.Default
    private List<RecipeImage> recipeImages = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeLike> recipeLikes = new ArrayList<>(); //TODO : RecipeLikes에 추가하는 메소드 필요!!

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @Builder.Default
    private List<RecipeComment> recipeComments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @Builder.Default
    private List<RecipeOrder> recipeOrders = new ArrayList<>();

    public void addRecipeImage(RecipeImage image) {
        image.setRecipe(this);
        this.recipeImages.add(image);
    }

    public void addRecipeOrder(RecipeOrder order) {
        order.setRecipe(this);
        this.recipeOrders.add(order);
    }

    public void addRecipeIngredient(RecipeIngredient ingredient) {
        ingredient.setRecipe(this);
        this.recipeIngredients.add(ingredient);
    }

    private void increaseLikes() {
        this.likes++;
    }

    private void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    //좋아요 추가 메서드
    public void addRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.add(recipeLike);
        this.increaseLikes();
        this.author.increasePointByRecipeLike();
    }

    //좋아요 제거 메서드
    public void removeRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.remove(recipeLike);
        this.decreaseLikes();
        this.author.decreasePointByRecipeLikeCancel();
    }

    //레시피 업데이트 관련 메서드들
    public void updateRecipe(RecipeRequestDTO.CreateRecipeDTO request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.recipeCategory = request.getRecipeCategory();
        this.cookingTimeMinutes = request.getCookingTimeMinutes();
        this.difficulty = request.getDifficulty();
        this.servings = request.getServings();
    }

    public void updateImages(List<RecipeImage> newImages) {
        this.recipeImages.clear();
        this.recipeImages.addAll(newImages);
    }

    public void updateIngredients(List<RecipeIngredient> newIngredients) {
        this.recipeIngredients.clear();
        this.recipeIngredients.addAll(newIngredients);
    }

    public void updateOrders(List<RecipeOrder> newOrders) {
        this.recipeOrders.clear();
        this.recipeOrders.addAll(newOrders);
    }
}