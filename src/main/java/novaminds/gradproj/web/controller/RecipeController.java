package novaminds.gradproj.web.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.security.auth.CurrentLoginId;
import novaminds.gradproj.security.auth.CurrentUser;
import novaminds.gradproj.web.dto.recipe.RecipeRequestDTO;
import novaminds.gradproj.web.dto.recipe.RecipeResponseDTO;
import novaminds.gradproj.service.RecipeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

	private final RecipeService recipeService;

	@PostMapping(value = "/create",
				 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ApiResponse<RecipeResponseDTO.CreateRecipeResultDTO> createRecipe(
			@CurrentUser User author,
			@Valid @RequestPart("data")RecipeRequestDTO.CreateRecipeDTO request,
			@RequestPart("recipeImages")List<MultipartFile> recipeImages,
			@RequestPart("stepImages")List<MultipartFile> stepImages
	){
		RecipeResponseDTO.CreateRecipeResultDTO result = recipeService.createRecipe(author, request, recipeImages, stepImages);
		return ApiResponse.onSuccess(result);
	}
}
