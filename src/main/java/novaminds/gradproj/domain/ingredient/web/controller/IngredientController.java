package novaminds.gradproj.domain.ingredient.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.ingredient.service.query.IngredientQueryService;
import novaminds.gradproj.domain.ingredient.web.dto.IngredientResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredients")
@Tag(name = "재료 관련 API", description = "재료 조회 및 검색하는 API 입니다.")
public class IngredientController {

	private final IngredientQueryService ingredientQueryService;

	//재료 검색 및 조회
	@Operation(summary = "식재료 조회 및 검색", description = "모든 식재료들을 조회합니다. 식재료 이름으로 검색할 수 있습니다.")
	@GetMapping()
	public ApiResponse<IngredientResponseDTO.IngredientListDTO> getIngredients(
		@RequestParam(required = false) String keyword
	) {
		var response = ingredientQueryService.getIngredients(keyword);
		return ApiResponse.onSuccess(response);
	}

}
