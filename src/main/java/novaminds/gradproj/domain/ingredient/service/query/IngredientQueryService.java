package novaminds.gradproj.domain.ingredient.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.ingredient.converter.IngredientConverter;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.domain.ingredient.web.dto.IngredientResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientQueryService {

	private final IngredientRepository ingredientRepository;

	public IngredientResponseDTO.IngredientListDTO getIngredients(String keyword){
		List<Ingredient> ingredients;
		if (StringUtils.hasText(keyword)){
			ingredients = ingredientRepository.findByIngredientNameContainingIgnoreCase(keyword);
		}else {
			ingredients = ingredientRepository.findAll();
		}

		return IngredientConverter.toIngredientListDTO(ingredients);
	}
}
