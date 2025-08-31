package novaminds.gradproj.domain.recipe.web.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import novaminds.gradproj.domain.recipe.service.command.RecipeCommandService;
import novaminds.gradproj.domain.recipe.service.query.RecipeQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeQueryService recipeQueryService;
    private final RecipeCommandService recipeCommandService;

	//레시피 등록
    @Operation(summary = "레시피 등록", description = "레시피를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
	@PostMapping("/create")
	public ApiResponse<Long> createRecipe(
			@CurrentUser Member member,
			@Valid @RequestBody RecipeRequestDTO.CreateRecipeDTO request
	){
		Long result = recipeCommandService.createRecipe(member, request);
		return ApiResponse.onSuccess(result);
	}

	//레시피 수정
	@PatchMapping(value = "/{recipeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "레시피 수정", description = "해당 레시피를 수정합니다.")
	public ApiResponse<RecipeResponseDTO.RecipeResultDTO> updateRecipe(
		@PathVariable("recipeId") Long recipeId,
		@CurrentUser Member member,
		@Valid @RequestPart("data") RecipeRequestDTO.RecipeUpdateDTO request,
		@RequestPart(value = "recipeImages", required = false) List<MultipartFile> newRecipeImages,
		@RequestPart(value = "stepImages", required = false) List<MultipartFile> newStepImages
	){
		RecipeResponseDTO.RecipeResultDTO result = recipeCommandService.updateRecipe(recipeId, member, request, newRecipeImages, newStepImages);
		return ApiResponse.onSuccess(result);
	}

	//레시피 삭제
	@DeleteMapping("/{recipeId}")
	@Operation(summary = "레시피 삭제", description = "해당 레시피를 삭제합니다.")
	public ApiResponse<String> deleteRecipe(
		@PathVariable("recipeId") Long recipeId,
		@CurrentUser Member member
	){
        recipeCommandService.deleteRecipe(recipeId, member);
		return ApiResponse.onSuccess("레시피가 성공적으로 삭제되었습니다.");
	}

	//레시피 상세 보기
	@GetMapping("/{recipeId}")
	@Operation(summary = "레시피 상세 조회", description = "특정 레시피 상세 내용 보기")
	public ApiResponse<RecipeResponseDTO.RecipeDetailDTO> getRecipeDetail(
			@PathVariable("recipeId") Long recipeId
	){
		RecipeResponseDTO.RecipeDetailDTO result = recipeQueryService.getRecipeDetail(recipeId);
		return ApiResponse.onSuccess(result);
	}

	//레시피 댓글 더 보기? 전체 보기.
	@GetMapping("/{recipeId}/comments")
	@Operation(summary = "레시피 댓글 전체 조회", description = "특정 레시피 댓글 전체 보기")
	public ApiResponse<Page<RecipeResponseDTO.CommentDTO>> getComments(
			@PathVariable("recipeId") Long recipeId,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
	) {
		Page<RecipeResponseDTO.CommentDTO> result = recipeQueryService.getComments(recipeId, pageable);
		return ApiResponse.onSuccess(result);
	}

	//카테고리 별 레시피.
	@GetMapping
	@Operation(summary = "카테고리 별 레시피 목록 조회", description = "카테고리 별로 레시피 목록을 조회")
	public ApiResponse<Page<RecipeResponseDTO.ListByCategoryDTO>> getRecipesByCategory(
			@RequestParam("category")RecipeCategory category,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	){
		Page<RecipeResponseDTO.ListByCategoryDTO> result = recipeQueryService.getRecipeByCategory(category, pageable);
		return ApiResponse.onSuccess(result);
	}

	//좋아요 추가 및 취소.
	@PostMapping("/{recipeId}/like")
	@Operation(summary = "레시피 좋아요 토글", description = "특정 레시피에 대한 좋아요를 추가하거나 취소합니다. (토글 방식)")
	public ApiResponse<RecipeResponseDTO.LikeDTO> toggleLike(
		@PathVariable("recipeId") Long recipeId,
		@CurrentUser Member member
	) {
		RecipeResponseDTO.LikeDTO result = recipeCommandService.RecipeLike(recipeId, member);
		return ApiResponse.onSuccess(result);
	}
}
