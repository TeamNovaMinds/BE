package novaminds.gradproj.web.dto.recipe;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.Difficulty;
import novaminds.gradproj.domain.Recipe.RecipeCategory;

public class RecipeRequestDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateRecipeDTO{

		@NotBlank(message = "레시피 제목은 필수입니다.")
		@Size(max=100, message = "레시피 제목은 최대 100자 입니다.")
		private String title;

		private String description;

		@NotNull(message = "레시피 카테고리는 필수입니다.")
		private RecipeCategory recipeCategory;

		@NotNull(message = "조리 시간은 필수입니다.")
		private Integer cookingTimeMinutes;

		@NotNull(message = "난이도는 필수입니다.")
		private Difficulty difficulty;

		private Integer servings;

		@NotNull(message = "재료 목록은 필수입니다.")
		@Size(min = 1, message = "재료는 최소 한 개 이상 필요합니다.")
		private List<RecipeIngredientDTO> ingredients;

		@NotNull(message = "요리 순서는 필수입니다.")
		@Size(min = 1, message = "요리 순서는 최소 1단계 이상 필요합니다.")
		private List<RecipeOrderDTO> orders;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeIngredientDTO{
		private Long ingredientId;
		private String description;
		private String amount;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeOrderDTO{
		private Integer order;
		private String description;
		private Integer imageIndex;
	}
}
