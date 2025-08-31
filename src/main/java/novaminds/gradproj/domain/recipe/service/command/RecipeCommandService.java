package novaminds.gradproj.domain.recipe.service.command;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.*;
import novaminds.gradproj.domain.recipe.repository.RecipeImageRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeLikeRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeRepository;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;
import novaminds.gradproj.domain.recipe.converter.RecipeConverter;

import static novaminds.gradproj.global.s3.service.PresignedS3Service.validateS3Urls;

import novaminds.gradproj.global.service.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeCommandService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeImageRepository recipeImageRepository;
    private final S3Service s3Service;

    //레시피 등록
    public Long createRecipe(
            Member member,
            RecipeRequestDTO.CreateRecipeDTO request
    ){
        // 1. S3 URL 검증
        validateS3Urls(request.getRecipeImages());

        // 2. 새 레시피 엔티티
        Recipe newRecipe = RecipeConverter.toRecipe(member, request);

        // 3. 요청에서 imageUrl 가져옴
        List<String> imageUrls = request.getRecipeImages();

        // 4. RecipeImage 엔티티 생성 및 추가
        imageUrls.stream()
                .map(imageUrl -> RecipeConverter.toRecipeImage(imageUrl, newRecipe, imageUrls.indexOf(imageUrl)))
                .forEach(newRecipe::addRecipeImage);


        // 5. RecipeOrder 엔티티 생성 및 추가
        request.getOrders().stream()
                .map(dto -> RecipeConverter.toRecipeOrder(dto, newRecipe))
                .forEach(newRecipe::addRecipeOrder);

        // 6. RecipeIngredient ID 리스트 추출
        List<Long> ingredientIds = request.getIngredients().stream()
                .map(RecipeRequestDTO.RecipeIngredientDTO::getIngredientId)
                .toList();

        // 7. 배치 조회
        Map<Long, Ingredient> ingredientMap = ingredientRepository.findAllById(ingredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

        // 8. 존재하지 않는 ingredientId 검증
        ingredientIds.forEach(id -> {
            if (!ingredientMap.containsKey(id)) {
                throw new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND);
            }
        });

        // 9. RecipeIngredient 엔티티 생성 및 추가
        request.getIngredients().stream()
                .map(dto -> RecipeConverter.toRecipeIngredient(dto, newRecipe, ingredientMap.get(dto.getIngredientId())))
                .forEach(newRecipe::addRecipeIngredient);

        // 10. Recipe 엔티티 저장
        Recipe savedRecipe = recipeRepository.save(newRecipe);

        return savedRecipe.getId();
    }

    //레시피 수정
    public RecipeResponseDTO.RecipeResultDTO updateRecipe(Long recipeId, Member member, RecipeRequestDTO.RecipeUpdateDTO request,
                                                          List<MultipartFile> newRecipeImages, List<MultipartFile> newStepImages) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        if (!recipe.getAuthor().getLoginId().equals(member.getLoginId())) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
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
                    .toList();
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
                .toList()
                : Collections.emptyList();

        if (request.getIngredients() != null) {
            List<RecipeIngredient> recipeIngredients = request.getIngredients().stream().map(dto -> {
                Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                        .orElseThrow(() -> new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND));
                return RecipeIngredient.builder().recipe(recipe).ingredient(ingredient).amount(dto.getAmount()).build();
            }).toList();
            recipe.getRecipeIngredients().addAll(recipeIngredients);
        }

        if (request.getOrders() != null) {
            List<RecipeOrder> recipeOrders = request.getOrders().stream().map(dto -> {
                String imageUrl = null;
//                if (dto.getImageIndex() != null && dto.getImageIndex() < stepImageUrls.size()) {
//                    imageUrl = stepImageUrls.get(dto.getImageIndex());
//                } TODO : 로직 수정 예정
                return RecipeOrder.builder()
                        .recipe(recipe)
                        .order(dto.getOrder())
                        .description(dto.getDescription())
                        .ImgUrl(imageUrl)
                        .build();
            }).toList();
            recipe.getRecipeOrders().addAll(recipeOrders);
        }

        return RecipeResponseDTO.RecipeResultDTO.builder()
                .recipeId(recipeId)
                .build();
    }

    //레시피 삭제
    public void deleteRecipe(Long recipeId, Member member){
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        if(!recipe.getAuthor().getLoginId().equals(member.getLoginId())){
            throw new GeneralException(ErrorStatus.RECIPE_DELETE_FORBIDDEN);
        }

        recipeRepository.delete(recipe);
    }

    //좋아요 추가 및 취소.
    @Transactional
    public RecipeResponseDTO.LikeDTO RecipeLike(Long recipeId, Member member){

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        Optional<RecipeLike> existLike = recipeLikeRepository.findByMemberLoginIdAndRecipeId(member.getLoginId(), recipeId);

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
                    .member(member)
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
