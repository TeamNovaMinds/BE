package novaminds.gradproj.domain.Recipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.BaseEntity;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.recipeingredient.RecipeIngredient;

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
    private int likes = 0;

    @Column(name = "recipe_img_url")
    private String recipeImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RecipeCategory recipeCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "cooking_time_minutes")
    private Integer cookingTimeMinutes; //TODO : 시간을 10 ~ 15분 이런식으로 표현하려면 나중에 수정 필요

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Difficulty difficulty;

    @Column
    private Integer servings; //몇 인분인지 나타내는 필드

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ingredientOrder ASC")
    @Builder.Default
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeLike> recipeLikes = new ArrayList<>(); //TODO : RecipeLikes에 추가하는 메소드 필요!!

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @Builder.Default
    private List<RecipeComment> recipeComments = new ArrayList<>();

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
    }

    //좋아요 제거 메서드
    public void removeRecipeLike(RecipeLike recipeLike) {
        this.recipeLikes.remove(recipeLike);
        this.decreaseLikes();
    }
}