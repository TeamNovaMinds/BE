package novaminds.gradproj.domain.recipe.converter;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeImage;
import novaminds.gradproj.domain.recipe.entity.RecipeIngredient;
import novaminds.gradproj.domain.recipe.entity.RecipeOrder;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;

public class RecipeConverter {

    //Recipe 엔티티 생성
    public static Recipe toRecipe(Member member, RecipeRequestDTO.CreateRecipeDTO request) {
        return Recipe.builder()
                .author(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .recipeCategory(request.getRecipeCategory())
                .cookingTimeMinutes(request.getCookingTimeMinutes())
                .difficulty(request.getDifficulty())
                .servings(request.getServings())
                .build();
    }

    // RecipeOrder 엔티티 생성
    public static RecipeOrder toRecipeOrder(RecipeRequestDTO.RecipeOrderDTO dto, Recipe recipe) {
        return RecipeOrder.builder()
                .recipe(recipe)
                .order(dto.getOrder())
                .description(dto.getDescription())
                .ImgUrl(dto.getStepImage())
                .build();
    }

    // RecipeImage 엔티티 생성
    public static RecipeImage toRecipeImage(String imageUrl, Recipe recipe, int index) {
        return RecipeImage.builder()
                .recipe(recipe)
                .imageUrl(imageUrl)
                .imageOrder(index)
                .isMain(index == 0)
                .build();
    }

    // RecipeIngredient 엔티티 생성
    public static RecipeIngredient toRecipeIngredient(RecipeRequestDTO.RecipeIngredientDTO dto, Recipe recipe, Ingredient ingredient) {
        return RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build();
    }
}
