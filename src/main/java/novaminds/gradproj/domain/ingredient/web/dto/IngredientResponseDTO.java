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

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IngredientDetailResponse {
		private Long id;
		private String name;
		private String category;
		private String imageUrl;
		private ShelfLifeDTO shelfLife; // 보관일 정보 추가
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ShelfLifeDTO {
		private int fridgeDays;
		private int freezerDays;
		private int roomTempDays;
	}
}
