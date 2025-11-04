package novaminds.gradproj.domain.recipe.web.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import novaminds.gradproj.domain.member.service.security.auth.CurrentLoginId;
import novaminds.gradproj.domain.recipe.service.command.RecipeCommandService;
import novaminds.gradproj.domain.recipe.service.query.RecipeQueryService;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.recipe.entity.RecipeSortType;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@Tag(name = "레시피 관련 API", description = "레시피 등록 수정 및 조회하는 API 입니다.")
public class RecipeController {

    private final RecipeQueryService recipeQueryService;
    private final RecipeCommandService recipeCommandService;

	//레시피 등록
    @Operation(summary = "레시피 등록", description = "레시피를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ApiResponse<Long> createRecipe(
			@CurrentUser Member member,
			@Valid @RequestBody RecipeRequestDTO.CreateRecipeDTO request
	){
		Long result = recipeCommandService.createRecipe(member, request);
		return ApiResponse.onSuccess(result);
	}

    //레시피 수정
    @Operation(summary = "레시피 수정", description = "레시피를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PutMapping("/{recipeId}")
    public ApiResponse<Long> updateRecipe(
            @CurrentLoginId String memberId,
            @PathVariable("recipeId") Long recipeId,
            @Valid @RequestBody RecipeRequestDTO.CreateRecipeDTO request
    ){
        Long result = recipeCommandService.updateRecipe(memberId, recipeId, request);
        return ApiResponse.onSuccess(result);
    }

    //레시피 삭제
    @Operation(summary = "레시피 삭제", description = "해당 레시피를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @DeleteMapping("/{recipeId}")
    public ApiResponse<String> deleteRecipe(
            @CurrentUser Member member,
            @PathVariable("recipeId") Long recipeId
    ) {
        recipeCommandService.deleteRecipe(member, recipeId);
        return ApiResponse.onSuccess("레시피가 성공적으로 삭제되었습니다.");
    }

    //카테고리 별 레시피.
    @Operation(summary = "카테고리 별 레시피 목록 조회", description = "카테고리 별로 레시피 목록을 조회")
    @Parameters({
            @Parameter(name = "category", description = "조회하려고 하는 category 종류 (없으면 모든 카테고리 동시 조회)", required = false, example = "KOREAN"),
            @Parameter(name = "keyword", description = "검색하려고 하는 레시피 이름", required = false, example = "군만두"),
            @Parameter(name = "sortBy", description = "정렬 기준 (LATEST: 최신순, LIKES: 좋아요순, 기본값: LATEST)", required = false, example = "LATEST"),
            @Parameter(name = "cursorId", description = "커서 ID (페이징을 위한 커서, 처음에는 null)", required = false, example = "10")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping
    public ApiResponse<RecipeResponseDTO.RecipeListResponse> getRecipes(
            @CurrentLoginId String memberId,
            @RequestParam(required = false) RecipeCategory category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "LATEST") RecipeSortType sortBy,
            @RequestParam(required = false) Long cursorId
    ) {
        var result = recipeQueryService.getRecipe(memberId, category, keyword, sortBy, cursorId);
        return ApiResponse.onSuccess(result);
    }

	//레시피 상세 보기
    @Operation(summary = "레시피 상세 조회", description = "특정 레시피 상세 내용 보기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
	@GetMapping("/{recipeId}")
	public ApiResponse<RecipeResponseDTO.RecipeDetailResponse> getRecipeDetail(
            @CurrentLoginId String memberId,
            @PathVariable("recipeId") Long recipeId
	){
		var result = recipeQueryService.getRecipeDetail(memberId, recipeId);
		return ApiResponse.onSuccess(result);
	}

	//레시피 댓글 더 보기? 전체 보기.
    @Operation(summary = "레시피 댓글 전체 조회", description = "특정 레시피 댓글 전체 보기")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
	@GetMapping("/{recipeId}/comments")
	public ApiResponse<RecipeResponseDTO.CommentListResponse> getComments(
            @CurrentLoginId String memberId,
            @PathVariable("recipeId") Long recipeId,
            @RequestParam(required = false) Long cursorId
	) {
		var result = recipeQueryService.getComments(memberId, recipeId, cursorId);
		return ApiResponse.onSuccess(result);
	}

    //좋아요 추가 및 취소.
    @Operation(summary = "레시피 좋아요 토글", description = "특정 레시피에 대한 좋아요를 추가하거나 취소합니다. (토글 방식)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @PostMapping("/{recipeId}/like")
    public ApiResponse<Long> toggleLike(
            @CurrentUser Member member,
            @PathVariable("recipeId") Long recipeId
    ) {
        boolean isLiked = recipeCommandService.toggleRecipeLike(member, recipeId);

        if (isLiked) {
            return ApiResponse.onSuccess("좋아요가 추가되었습니다.", recipeId);
        } else {
            return ApiResponse.onSuccess("좋아요가 취소되었습니다.", recipeId);
        }
    }

    @Operation(summary = "레시피 댓글 작성 API", description = "레시피에 댓글 또는 대댓글을 작성합니다.")
    @Parameters({
            @Parameter(name = "recipeId", description = "댓글을 작성할 레시피 ID", required = true, example = "1"),
            @Parameter(name = "parentCommentId", description = "대댓글 작성 시 부모 댓글 ID (대댓글이 아닌 경우 생략 가능)", required = false, example = "10")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{recipeId}/comments")
    public ApiResponse<Long> createComment(
            @CurrentUser Member member,
            @PathVariable Long recipeId,
            @RequestParam(required = false) Long parentCommentId,
            @Valid @RequestBody RecipeRequestDTO.CommentCreateRequest request
    ) {
        Long resultCommentId = recipeCommandService.createComment(member, recipeId, parentCommentId, request);
        return ApiResponse.onSuccess(resultCommentId);
    }

    @Operation(
            summary = "냉장고 재료 기반 레시피 추천",
            description = """
                    냉장고 보관 재료로 만들 수 있는 레시피를 추천합니다.
                    • 특정 재료 선택 시: 해당 재료로 만드는 모든 레시피 추천
                    • 재료 미선택 시: 현재 보관 중인 모든 재료로 만드는 레시피 추천
                    """
    )
    @Parameters({
            @Parameter(name = "storageType", description = "보관 방식 (REFRIGERATED, FROZEN, ROOM_TEMPERATURE)", required = true, example = "REFRIGERATOR"),
            @Parameter(name = "keyword", description = "레시피를 추천 받고 싶은 재료 검색", required = false, example = "테스트"),
            @Parameter(name = "storedItemId", description = "추천받고 싶은 특정 재료 ID (선택사항)", required = false, example = "1"),
            @Parameter(name = "cursorId", description = "커서 ID (페이징을 위한 커서, 처음에는 null)", required = false, example = "10")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/suggest")
    public ApiResponse<RecipeResponseDTO.SuggestedRecipeListResponse> suggestRecipesByIngredients(
            @CurrentUser Member member,
            @RequestParam StorageType storageType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long storedItemId,
            @RequestParam(required = false) Long cursorId
    ) {
        var result = recipeQueryService.suggestRecipesByIngredients(member, storageType, keyword, storedItemId, cursorId);
        return ApiResponse.onSuccess(result);
    }

    //내가 작성한 레시피 조회
    @Operation(summary = "내가 작성한 레시피 목록 조회", description = "현재 로그인한 사용자가 작성한 레시피 목록을 조회합니다.")
    @Parameters({
            @Parameter(name = "sortBy", description = "정렬 기준 (LATEST: 최신순, LIKES: 좋아요순, 기본값: LATEST)", required = false, example = "LATEST"),
            @Parameter(name = "cursorId", description = "커서 ID (페이징을 위한 커서, 처음에는 null)", required = false, example = "10")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/my")
    public ApiResponse<RecipeResponseDTO.RecipeListResponse> getMyRecipes(
            @CurrentLoginId String memberId,
            @RequestParam(required = false, defaultValue = "LATEST") RecipeSortType sortBy,
            @RequestParam(required = false) Long cursorId
    ) {
        var result = recipeQueryService.getMyRecipes(memberId, sortBy, cursorId);
        return ApiResponse.onSuccess(result);
    }

    //내가 좋아요 누른 레시피 조회
    @Operation(summary = "내가 좋아요 누른 레시피 목록 조회", description = "현재 로그인한 사용자가 좋아요 누른 레시피 목록을 조회합니다.")
    @Parameters({
            @Parameter(name = "sortBy", description = "정렬 기준 (LATEST: 최신순, LIKES: 좋아요순, 기본값: LATEST)", required = false, example = "LATEST"),
            @Parameter(name = "cursorId", description = "커서 ID (페이징을 위한 커서, 처음에는 null)", required = false, example = "10")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/liked")
    public ApiResponse<RecipeResponseDTO.RecipeListResponse> getLikedRecipes(
            @CurrentLoginId String memberId,
            @RequestParam(required = false, defaultValue = "LATEST") RecipeSortType sortBy,
            @RequestParam(required = false) Long cursorId
    ) {
        var result = recipeQueryService.getLikedRecipes(memberId, sortBy, cursorId);
        return ApiResponse.onSuccess(result);
    }

}
