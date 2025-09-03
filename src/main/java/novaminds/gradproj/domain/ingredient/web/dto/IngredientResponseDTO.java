package novaminds.gradproj.domain.ingredient.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IngredientResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IngredientDTO{
		private Long id;
		private String name;
		private String category;
		private String imageUrl;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IngredientListDTO {
		private List<IngredientDTO> ingredients;
	}
}
