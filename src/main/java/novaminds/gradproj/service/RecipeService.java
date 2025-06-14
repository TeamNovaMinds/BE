package novaminds.gradproj.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.Recipe.Recipe;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
import novaminds.gradproj.domain.Recipe.RecipeComment;
import novaminds.gradproj.domain.Recipe.RecipeCommentRepository;
import novaminds.gradproj.domain.Recipe.RecipeImage;
import novaminds.gradproj.domain.Recipe.RecipeImageRepository;
import novaminds.gradproj.domain.Recipe.RecipeIngredient;
import novaminds.gradproj.domain.Recipe.RecipeLike;
import novaminds.gradproj.domain.Recipe.RecipeLikeRepository;
import novaminds.gradproj.domain.Recipe.RecipeOrder;
import novaminds.gradproj.domain.Recipe.RecipeRepository;
import novaminds.gradproj.domain.ingredient.Ingredient;
import novaminds.gradproj.domain.ingredient.IngredientRepository;
import novaminds.gradproj.domain.user.User;
import novaminds.gradproj.domain.user.UserRepository;
import novaminds.gradproj.web.dto.recipe.RecipeRequestDTO;
import novaminds.gradproj.web.dto.recipe.RecipeResponseDTO;

@Service
@RequiredArgsConstructor
public class RecipeService {

	private final RecipeRepository recipeRepository;
	private final S3Service s3Service;
	private final IngredientRepository ingredientRepository;
	private final RecipeCommentRepository recipeCommentRepository;
	private final RecipeLikeRepository recipeLikeRepository;
	private final RecipeImageRepository recipeImageRepository;

	//레시피 등록
	@Transactional
	public RecipeResponseDTO.CreateRecipeResultDTO createRecipe(
				User author,
				RecipeRequestDTO.CreateRecipeDTO request,
				List<MultipartFile> recipeImages,
				List<MultipartFile> stepImages
	){

		//1. 작성자 @CurrentUser

		//2. 새 레시피 엔티티
		Recipe newRecipe = Recipe.builder()
			.author(author)
			.title(request.getTitle())
			.description(request.getDescription())
			.recipeCategory(request.getRecipeCategory())
			.cookingTimeMinutes(request.getCookingTimeMinutes())
			.difficulty(request.getDifficulty())
			.servings(request.getServings())
			.build();

		//3. 이미지 업로드 및 RecipeImage 엔티티
		List<String> imageUrls = recipeImages.stream()
			.map(image -> s3Service.uploadFile(image, "recipe-image"))
			.collect(Collectors.toList());

		List<RecipeImage> recipeImageList = new ArrayList<>();

		for (int i = 0; i< imageUrls.size(); i++){
			RecipeImage recipeImage = RecipeImage.builder()
				.recipe(newRecipe)
				.imageUrl(imageUrls.get(i))
				.imageOrder(i)
				.isMain(i==0)
				.build();
			recipeImageList.add(recipeImage);
		}
		newRecipe.getRecipeImages().addAll(recipeImageList);

		//4. RecipeOrder의 사진 S3 업로드
		List<String> stepImageUrls = (stepImages != null && !stepImages.isEmpty())
			? stepImages.stream()
				.map(image->s3Service.uploadFile(image, "recipe-step-images"))
				.collect(Collectors.toList())
			: Collections.emptyList();

		//5. RecipeOrder 엔티티
		List<RecipeOrder> recipeOrders = request.getOrders().stream()
			.map(dto->{
				String imageUrl = null;
				if(dto.getImageIndex() != null && dto.getImageIndex()<stepImageUrls.size()){
					imageUrl = stepImageUrls.get(dto.getImageIndex());
				}
				return RecipeOrder.builder()
					.recipe(newRecipe)
					.order(dto.getOrder())
					.description(dto.getDescription())
					.ImgUrl(imageUrl)
					.build();
			})
			.collect(Collectors.toList());
		newRecipe.getRecipeOrders().addAll(recipeOrders);

		//6. RecipeIngredient 엔티티
		List<RecipeIngredient> recipeIngredients = request.getIngredients().stream()
			.map(dto->{
				Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
					.orElseThrow(()-> new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND));
				return RecipeIngredient.builder()
					.recipe(newRecipe)
					.ingredient(ingredient)
					.description(dto.getDescription())
					.amount(dto.getAmount())
					.build();
			}).collect(Collectors.toList());
		newRecipe.getRecipeIngredients().addAll(recipeIngredients);


		Recipe savedRecipe = recipeRepository.save(newRecipe);

		//리턴 값을 일단은 게시글 아이디로 했는데, 나중에 변경해야 될 수도.
		//바로그냥 게시글 상세보기로 리다이렉트?
		return RecipeResponseDTO.CreateRecipeResultDTO.builder()
			.recipeId(savedRecipe.getId())
			.build();

	}

	//레시피 수정
	@Transactional
	public void updateRecipe(Long recipeId, User user, RecipeRequestDTO.RecipeUpdateDTO request,
		List<MultipartFile> newRecipeImages, List<MultipartFile> newStepImages) {
		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

		if (!recipe.getAuthor().getLoginId().equals(user.getLoginId())) {
			throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
		}

		//기본 정보 업뎃
		if (request.getTitle() != null)
			recipe.updateTitle(request.getTitle());
		if (request.getDescription() != null)
			recipe.updateDescription(request.getDescription());
		if (request.getRecipeCategory() != null)
			recipe.updateRecipeCategory(request.getRecipeCategory());
		if (request.getCookingTimeMinutes() != null)
			recipe.updateCookingTimeMinutes(request.getCookingTimeMinutes());
		if (request.getDifficulty() != null)
			recipe.updateDifficulty(request.getDifficulty());
		if (request.getServings() != null)
			recipe.updateServings(request.getServings());

		//완성 사진 업데이트
		if (request.getDeletedRecipeImages() != null && !request.getDeletedRecipeImages().isEmpty()) {
			recipeImageRepository.deleteAllById(request.getDeletedRecipeImages());
		}
		if (newRecipeImages != null && !newRecipeImages.isEmpty()) {
			List<String> recipeImageUrls = newRecipeImages.stream()
				.map(image -> s3Service.uploadFile(image, "recipe-images"))
				.collect(Collectors.toList());
			for (int i = 0; i < recipeImageUrls.size(); i++) {
				RecipeImage recipeImage = RecipeImage.builder()
					.recipe(recipe)
					.imageUrl(recipeImageUrls.get(i))
					.imageOrder(recipe.getRecipeImages().size() + i) // 기존 이미지 순서 뒤에 붙임
					.isMain(false) // 메인 이미지 변경 로직은 별도 구현 필요
					.build();
				recipe.getRecipeImages().add(recipeImage);
			}
		}

		//order 업데이트 (기존 것 모두 삭제 후, 요청받은 것으로 새로 추가)
		recipe.getRecipeIngredients().clear();
		recipe.getRecipeOrders().clear();

		List<String> stepImageUrls = (newStepImages != null && !newStepImages.isEmpty())
			? newStepImages.stream()
			.map(image -> s3Service.uploadFile(image, "recipe-step-images"))
			.collect(Collectors.toList())
			: Collections.emptyList();

		if (request.getIngredients() != null) {
			List<RecipeIngredient> recipeIngredients = request.getIngredients().stream().map(dto -> {
				Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
					.orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND)); // TODO: INGREDIENT_NOT_FOUND
				return RecipeIngredient.builder().recipe(recipe).ingredient(ingredient).amount(dto.getAmount()).build();
			}).collect(Collectors.toList());
			recipe.getRecipeIngredients().addAll(recipeIngredients);
		}

		if (request.getOrders() != null) {
			List<RecipeOrder> recipeOrders = request.getOrders().stream().map(dto -> {
				String imageUrl = null;
				if (dto.getImageIndex() != null && dto.getImageIndex() < stepImageUrls.size()) {
					imageUrl = stepImageUrls.get(dto.getImageIndex());
				}
				return RecipeOrder.builder()
					.recipe(recipe)
					.order(dto.getOrder())
					.description(dto.getDescription())
					.ImgUrl(imageUrl)
					.build();
			}).collect(Collectors.toList());
			recipe.getRecipeOrders().addAll(recipeOrders);
		}
	}

		//레시피 삭제
	@Transactional
	public void deleteRecipe(Long recipeId, User user){
		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

		if(!recipe.getAuthor().getLoginId().equals(user.getLoginId())){
			throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
		}

		recipeRepository.delete(recipe);
	}


	//레시피 상세
	@Transactional(readOnly = true)
	public RecipeResponseDTO.RecipeDetailDTO getRecipeDetail(Long recipeId){

		//레시피 아이디로 가져오기
		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

		//미리보기 댓글 가져오기
		List<RecipeComment> previewComments = recipeCommentRepository
			.findTop3ByRecipeIdAndParentCommentIsNullOrderByCreatedAtDesc(recipeId);

		return RecipeResponseDTO.RecipeDetailDTO.builder()
			.recipeId(recipe.getId())
			.title(recipe.getTitle())
			.description(recipe.getDescription())
			.recipeCategory(recipe.getRecipeCategory())
			.cookingTimeMinutes(recipe.getCookingTimeMinutes())
			.difficulty(recipe.getDifficulty())
			.servings(recipe.getServings())
			.createdAt(recipe.getCreatedAt())
			.likeCount(recipe.getRecipeLikes().size())
			.commentPreview(
				RecipeResponseDTO.CommentPreviewDTO.builder()
					.totalCount(recipe.getRecipeComments().size())
					.previewComments(previewComments.stream()
						.map(RecipeResponseDTO.CommentDTO::from)
						.collect(Collectors.toList()))
					.build()
			)
			.author(RecipeResponseDTO.AuthorDTO.from(recipe.getAuthor()))
			.recipeImages(recipe.getRecipeImages().stream()
				.map(RecipeResponseDTO.ImageDTO::from)
				.collect(Collectors.toList()))
			.ingredients(recipe.getRecipeIngredients().stream()
				.map(RecipeResponseDTO.IngredientDTO::from)
				.collect((Collectors.toList())))
			.orders(recipe.getRecipeOrders().stream()
				.map(RecipeResponseDTO.OrderDTO::from)
				.collect(Collectors.toList()))
			.build();
	}

	//댓글 더보기 기능
	@Transactional(readOnly = true)
	public Page<RecipeResponseDTO.CommentDTO> getComments(Long recipeId, Pageable pageable) {
		if (!recipeRepository.existsById(recipeId)) {
			throw new GeneralException(ErrorStatus.USER_NOT_FOUND); // TODO: ErrorStatus에 RECIPE_NOT_FOUND 추가 후 변경
		}
		Page<RecipeComment> comments = recipeCommentRepository.findByRecipeIdAndParentCommentIsNull(recipeId, pageable);
		return comments.map(RecipeResponseDTO.CommentDTO::from);
	}

	//category 별 레시피 리스트 조회
	@Transactional(readOnly = true)
	public Page<RecipeResponseDTO.ListByCategoryDTO> getRecipeByCategory(RecipeCategory category, Pageable pageable){

		Page<Recipe> recipes = recipeRepository.findByRecipeCategory(category, pageable);

		return recipes.map(recipe ->{

			String mainImageUrl = recipe.getRecipeImages().stream()
				.filter(RecipeImage::isMain)
				.map(RecipeImage::getImageUrl)
				.findFirst()
				.orElse(recipe.getRecipeImages().isEmpty() ? null : recipe.getRecipeImages().get(0).getImageUrl());

			return  RecipeResponseDTO.ListByCategoryDTO.builder()
				.recipeId(recipe.getId())
				.title(recipe.getTitle())
				.mainImageUrl(mainImageUrl)
				.authorNickname(recipe.getAuthor().getNickname())
				.likeCount(recipe.getRecipeLikes().size())
				.commentCount(recipe.getRecipeComments().size())
				.createdAt(recipe.getCreatedAt())
				.build();
		});
	}

	//좋아요 추가 및 취소.
	@Transactional
	public RecipeResponseDTO.LikeDTO RecipeLike(Long recipeId, User user){

		Recipe recipe = recipeRepository.findById(recipeId)
			.orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

		Optional<RecipeLike> existLike = recipeLikeRepository.findByUserLoginIdAndRecipeId(user.getLoginId(), recipeId);

		//front에서 총 좋아요 개수와 함께 반환해주는 값
		//지금 응답이 좋아요 추가인지 취소인지 보기 쉽게 판단 도와주려고.
		boolean isLiked;

		if (existLike.isPresent()){
			RecipeLike recipeLike = existLike.get();
			recipe.removeRecipeLike(recipeLike);
			recipeLikeRepository.delete(recipeLike);
			isLiked = false;
		}else {
			RecipeLike newLike = RecipeLike.builder()
				.user(user)
				.recipe(recipe)
				.build();
			recipe.addRecipeLike(newLike);
			recipeLikeRepository.save(newLike);
			isLiked = true;
		}

		return RecipeResponseDTO.LikeDTO.builder()
			.recipeId(recipe.getId())
			.isLiked(isLiked)
			.likeCount(recipe.getRecipeLikes().size())
			.build();
	}
}
