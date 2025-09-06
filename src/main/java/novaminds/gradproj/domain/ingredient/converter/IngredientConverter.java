package novaminds.gradproj.domain.ingredient.converter;

import java.util.List;
import java.util.stream.Collectors;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.entity.ShelfLife;
import novaminds.gradproj.domain.ingredient.web.dto.IngredientResponseDTO;

public class IngredientConverter {

	public static IngredientResponseDTO.IngredientDTO toIngredientDTO(Ingredient ingredient){
		return IngredientResponseDTO.IngredientDTO.builder()
			.id(ingredient.getId())
			.name(ingredient.getIngredientName())
			.category(ingredient.getIngredientCategory().getIngredientCategoryName())
			.imageUrl(ingredient.getImageUrl())
			.build();
	}

	public static IngredientResponseDTO.IngredientListDTO toIngredientListDTO(List<Ingredient> ingredients){

		List<IngredientResponseDTO.IngredientDTO> ingredientDTOs = ingredients.stream()
			.map(IngredientConverter::toIngredientDTO)
			.collect(Collectors.toList());

		return IngredientResponseDTO.IngredientListDTO.builder()
			.ingredients(ingredientDTOs)
			.build();
	}

	public static IngredientResponseDTO.IngredientDetailResponse toIngredientDetailResponse(Ingredient ingredient) {
		return IngredientResponseDTO.IngredientDetailResponse.builder()
			.id(ingredient.getId())
			.name(ingredient.getIngredientName())
			.category(ingredient.getIngredientCategory().getIngredientCategoryName())
			.imageUrl(ingredient.getImageUrl())
			.shelfLife(toShelfLifeDTO(ingredient.getShelfLife()))
			.build();
	}

	private static IngredientResponseDTO.ShelfLifeDTO toShelfLifeDTO(ShelfLife shelfLife) {
		return IngredientResponseDTO.ShelfLifeDTO.builder()
			.fridgeDays(shelfLife.getFridgeDays())
			.freezerDays(shelfLife.getFreezerDays())
			.roomTempDays(shelfLife.getRoomTempDays())
			.build();
	}
}
