package novaminds.gradproj.domain.ingredient.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.ingredient.converter.IngredientConverter;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.entity.IngredientCategory;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.domain.ingredient.web.dto.IngredientResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientQueryService {

	private final IngredientRepository ingredientRepository;

	//재료 검색
	public IngredientResponseDTO.IngredientListDTO getIngredients(String keyword, IngredientCategory category){
		List<Ingredient> ingredients;

		// 키워드나 카테고리가 있으면 검색
		if (StringUtils.hasText(keyword) || category != null) {
			String processedKeyword = StringUtils.hasText(keyword)
				? keyword.toLowerCase().replaceAll("\\s+", "")
				: null;
			ingredients = ingredientRepository.searchIngredients(processedKeyword, category);
		}
		// 키워드와 카테고리가 모두 없으면 전체 조회
		else {
			ingredients = ingredientRepository.findAll();
		}

		return IngredientConverter.toIngredientListDTO(ingredients);
	}

	// 재료 초기 정보 조회
	public IngredientResponseDTO.IngredientDetailResponse getIngredientDetail(Long ingredientId) {
		Ingredient ingredient = ingredientRepository.findById(ingredientId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND));
		return IngredientConverter.toIngredientDetailResponse(ingredient);
	}
}
