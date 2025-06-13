package novaminds.gradproj.web.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
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

	//레시피 등록
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

	//레시피 상세 보기
	@GetMapping("/{recipeId}")
	@Operation(summary = "레시피 상세 조회", description = "특정 레시피 상세 내용 보기")
	public ApiResponse<RecipeResponseDTO.RecipeDetailDTO> getRecipeDetail(
			@PathVariable("recipeId") Long recipeId
	){
		RecipeResponseDTO.RecipeDetailDTO result = recipeService.getRecipeDetail(recipeId);
		return ApiResponse.onSuccess(result);
	}

	//레시피 댓글 더 보기? 전체 보기.
	@GetMapping("/{recipeId}/comments")
	@Operation(summary = "레시피 댓글 전체 조회", description = "특정 레시피 댓글 전체 보기")
	public ApiResponse<Page<RecipeResponseDTO.CommentDTO>> getComments(
			@PathVariable("recipeId") Long recipeId,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
	) {
		Page<RecipeResponseDTO.CommentDTO> result = recipeService.getComments(recipeId, pageable);
		return ApiResponse.onSuccess(result);
	}

	//카테고리 별 레시피.
	@GetMapping
	@Operation(summary = "카테고리 별 레시피 목록 조회", description = "카테고리 별로 레시피 목록을 조회")
	public ApiResponse<Page<RecipeResponseDTO.ListByCategoryDTO>> getRecipesByCategory(
			@RequestParam("category")RecipeCategory category,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	){
		Page<RecipeResponseDTO.ListByCategoryDTO> result = recipeService.getRecipeByCategory(category, pageable);
		return ApiResponse.onSuccess(result);
	}

	//좋아요 추가 및 취소.
	@PostMapping("/{recipeId}/like")
	@Operation(summary = "레시피 좋아요 토글", description = "특정 레시피에 대한 좋아요를 추가하거나 취소합니다. (토글 방식)")
	public ApiResponse<RecipeResponseDTO.LikeDTO> toggleLike(
		@PathVariable("recipeId") Long recipeId,
		@CurrentUser User user
	) {
		RecipeResponseDTO.LikeDTO result = recipeService.RecipeLike(recipeId, user);
		return ApiResponse.onSuccess(result);
	}
}
