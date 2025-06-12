package novaminds.gradproj.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.Recipe.Recipe;
import novaminds.gradproj.domain.Recipe.RecipeImage;
import novaminds.gradproj.domain.Recipe.RecipeIngredient;
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
	private final UserRepository userRepository;
	private final S3Service s3Service;
	private final IngredientRepository ingredientRepository;

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
}
