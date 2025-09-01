package novaminds.gradproj.domain.recipe.service.command;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.ingredient.repository.IngredientRepository;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.*;
import novaminds.gradproj.domain.recipe.repository.RecipeLikeRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeCommentRepository;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.domain.recipe.converter.RecipeConverter;

import static novaminds.gradproj.global.s3.service.S3Service.validateS3Urls;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeCommandService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeCommentRepository recipeCommentRepository;

    /**
     * 새로운 레시피를 생성하고 데이터베이스에 저장
     *
     * @param member 레시피 작성하는 회원
     * @param request 레시피 생성에 필요한 정보
     *
     * @return 생성된 레시피의 ID
     */
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
        IntStream.range(0, imageUrls.size())
                .mapToObj(i -> RecipeConverter.toRecipeImage(imageUrls.get(i), newRecipe, i))
                .forEach(newRecipe::addRecipeImage);

        // 5. RecipeOrder 엔티티 생성 및 추가
        request.getOrders().stream()
                .map(dto -> RecipeConverter.toRecipeOrder(dto, newRecipe))
                .forEach(newRecipe::addRecipeOrder);

        // 6. RecipeIngredient ID 리스트 추출
        List<Long> ingredientIds = request.getIngredients().stream()
                .map(RecipeRequestDTO.RecipeIngredientDTO::getIngredientId)
                .toList();

        // 7. ID 리스트들을 통해 한번에 배치 조회
        Map<Long, Ingredient> ingredientMap = validateAndGetIngredients(ingredientIds);

        // 8. RecipeIngredient 엔티티 생성 및 추가
        request.getIngredients().stream()
                .map(dto -> RecipeConverter.toRecipeIngredient(dto, newRecipe, ingredientMap.get(dto.getIngredientId())))
                .forEach(newRecipe::addRecipeIngredient);

        // 9. Recipe 엔티티 저장
        Recipe savedRecipe = recipeRepository.save(newRecipe);

        return savedRecipe.getId();
    }

    /**
     * 레시피 정보를 수정
     *
     * @param memberId 레시피 수정하는 회원의 ID
     * @param recipeId 수정할 레시피의 ID
     * @param request 수정할 레시피 정보
     *
     * @return 수정된 레시피의 ID
     */
    public Long updateRecipe(
            String memberId,
            Long recipeId,
            RecipeRequestDTO.CreateRecipeDTO request
    ) {
        // 1. 수정할 레시피 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        // 2. 레시피의 수정 권한 확인
        if (!recipe.getAuthor().getLoginId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.RECIPE_NOT_AUTHORIZED);
        }

        // 3. 레시피 기본 정보(제목, 설명, 카테고리) 업데이트
        recipe.updateRecipe(request);

        // 4. 레시피와 연관된 엔티티들(이미지, 재료, 순서) 업데이트
        updateRecipeImages(recipe, request.getRecipeImages());
        updateRecipeIngredients(recipe, request.getIngredients());
        updateRecipeOrders(recipe, request.getOrders());

        return recipeId;
    }

    /**
     * 레시피를 삭제
     *
     * @param member 레시피 삭제하는 회원의 ID
     * @param recipeId 삭제할 레시피의 ID
     */
    public void deleteRecipe(Member member, Long recipeId){
        // 1. 삭제할 레시피 조회
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(()->new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        // 2. 레시피 삭제 권한 확인
        if(!recipe.getAuthor().getLoginId().equals(member.getLoginId())){
            throw new GeneralException(ErrorStatus.RECIPE_NOT_AUTHORIZED);
        }

        // 3. 레시피를 삭제합니다.
        recipeRepository.delete(recipe);
    }

    //좋아요 추가 및 취소.

    /**
     * 레시피에 좋아요 추가 및 취소
     *
     * @param member 레시피에 좋아요를 누르는 회원의 ID
     * @param recipeId 좋아요를 누르거나 취소할 레시피의 ID
     *
     * @return 해당 레시피에 좋아요를 누르면 true, 취소하면 false 반환
     */
    @Transactional
    public boolean toggleRecipeLike(Member member, Long recipeId) {
        // 1. 레시피를 조회합니다. 없으면 예외가 발생합니다.
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        // 2. 사용자의 좋아요 존재 여부를 확인하고, 결과에 따라 분기 처리합니다.
        return recipeLikeRepository.findByMemberLoginIdAndRecipeId(member.getLoginId(), recipeId)
                .map(recipeLike -> {
                    // 3-1. 좋아요가 이미 존재하면(map 실행), 삭제 로직을 호출하고 false를 반환합니다.
                    deleteRecipeLike(recipe, recipeLike);
                    return false;
                })
                .orElseGet(() -> {
                    // 3-2. 좋아요가 존재하지 않으면(orElseGet 실행), 생성 로직을 호출하고 true를 반환합니다.
                    createAndSaveRecipeLike(recipe, member);
                    return true;
                });
    }

    /**
     * 레시피 이미지들을 새 이미지로 교체
     * 기존 목록을 다 지우고 새 목록으로 전체 교체하는 방식 사용
     *
     * @param recipe 이미지를 수정할 Recipe 엔티티
     * @param imageUrls 새로운 이미지 URL 목록
     */
    private void updateRecipeImages(Recipe recipe, List<String> imageUrls) {

        // 1. 업데이트할 RecipeImage URL 목록이 null이면 바로 종료
        if (imageUrls == null) return;

        // 2. S3 URL 유효성 검증
        validateS3Urls(imageUrls);

        // 3. RecipeImage List로 변환
        List<RecipeImage> newImages = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> RecipeConverter.toRecipeImage(imageUrls.get(i), recipe, i))
                .toList();

        // 4. RecipeImage를 새 RecipeImage로 교체
        // updateImages() 메소드 내부 로직에 의해 기존의 이미지들은 고아객체가 되어서 삭제됨
        recipe.updateImages(newImages);
    }

    /**
     * 레시피 재료 목록을 새로운 재료로 교체
     * 기존 목록을 다 지우고 새 목록으로 전체 교체하는 방식 사용
     *
     * @param recipe 재료를 수정할 Recipe 엔티티
     * @param ingredientDTOs 새로운 재료 목록
     */
    private void updateRecipeIngredients(Recipe recipe, List<RecipeRequestDTO.RecipeIngredientDTO> ingredientDTOs) {

        // 1. 업데이트할 Ingredient 목록이 null이면 바로 종료
        if (ingredientDTOs == null) return;

        // 2. Ingredient ID List 추출
        List<Long> ingredientIds = ingredientDTOs.stream()
                .map(RecipeRequestDTO.RecipeIngredientDTO::getIngredientId)
                .toList();

        // 3. ID 리스트들을 통해 한번에 배치 조회
        Map<Long, Ingredient> ingredientMap = validateAndGetIngredients(ingredientIds);

        // 4. RecipeIngredient List로 변환
        List<RecipeIngredient> newIngredients = ingredientDTOs.stream()
                .map(dto -> RecipeConverter.toRecipeIngredient(dto, recipe, ingredientMap.get(dto.getIngredientId())))
                .toList();

        // 5. RecipeIngredient를 새 RecipeIngredient로 교체
        recipe.updateIngredients(newIngredients);
    }

    /**
     * 레시피 조리 순서 목록을 새로운 목록으로 교체
     * 기존 목록을 다 지우고 새 목록으로 전체 교체하는 방식 사용
     *
     * @param recipe 조리 순서를 수정할 Recipe 엔티티
     * @param orderDTOs 새로운 조리 순서 정보 DTO 목록
     */
    private void updateRecipeOrders(Recipe recipe, List<RecipeRequestDTO.RecipeOrderDTO> orderDTOs) {

        // 1. 업데이트할 RecipeOrder 목록이 null이면 바로 종료
        if (orderDTOs == null) return;

        // 2. RecipeOrder List로 변환
        List<RecipeOrder> newOrders = orderDTOs.stream()
                .map(dto -> RecipeConverter.toRecipeOrder(dto, recipe))
                .toList();

        // RecipeOrder를 새 RecipeOrder로 교체
        recipe.updateOrders(newOrders);
    }

    /**
     * 재료 ID 목록으로 재료들을 한번에 배치 조회
     * 재료 ID 가 실제로 존재하는지 확인하고, Ingredient 맵을 반환
     *
     * @param ingredientIds 조회할 재료 ID 목록
     * @return 재료 ID를 키로 하는 Ingredient 맵
     */
    private Map<Long, Ingredient> validateAndGetIngredients(List<Long> ingredientIds) {

        // 1. Ingredient ID 목록을 통해 한번에 배치 조회
        Map<Long, Ingredient> ingredientMap = ingredientRepository.findAllById(ingredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getId, ingredient -> ingredient));

        // 2. 요청에 담겨온 모든 ID 목록이 전부 조회되었는지 검증
        // 만약 존재하지 않는 ID가 요청되었으면 예외 발생
        ingredientIds.forEach(id -> {
            if (!ingredientMap.containsKey(id)) {
                throw new GeneralException(ErrorStatus.INGREDIENT_NOT_FOUND);
            }
        });

        return ingredientMap;
    }

    /**
     * RecipeLike 엔티티를 삭제하고 Recipe의 좋아요 개수를 감소
     * .
     * @param recipe 좋아요가 취소될 레시피
     * @param recipeLike 삭제할 RecipeLike 엔티티
     */
    private void deleteRecipeLike(Recipe recipe, RecipeLike recipeLike) {
        recipeLikeRepository.delete(recipeLike);
        recipe.removeRecipeLike(recipeLike); // 좋아요 수 및 작성자 포인트 감소 로직 호출
    }

    /**
     * 새로운 RecipeLike 엔티티를 생성 및 저장하고 Recipe의 좋아요 개수를 증가시.
     *
     * @param recipe 좋아요가 추가될 레시피
     * @param member 좋아요를 누른 회원
     */
    private void createAndSaveRecipeLike(Recipe recipe, Member member) {
        RecipeLike newLike = RecipeLike.builder()
                .member(member)
                .recipe(recipe)
                .build();
        recipeLikeRepository.save(newLike);
        recipe.addRecipeLike(newLike); // 좋아요 수 및 작성자 포인트 증가 로직 호출
    }

    /**
     * 레시피에 댓글을 작성.
     *
     * @param member 댓글 작성하는 회원
     * @param recipeId 댓글을 작성할 레시피 ID
     * @param parentCommentId 대댓글인 경우 부모 댓글 ID (null이면 일반 댓글)
     * @param request 댓글 내용
     *
     * @return 생성된 댓글의 ID
     */
    public Long createComment(
            Member member,
            Long recipeId,
            Long parentCommentId,
            RecipeRequestDTO.CommentCreateRequest request
    ) {
        // 1. 레시피 존재 확인
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        // 2. 대댓글인 경우 부모 댓글 존재 확인
        RecipeComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = recipeCommentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
            
            // 부모 댓글이 같은 레시피에 속하는지 확인
            if (!recipeCommentRepository.existsByIdAndRecipeId(parentCommentId, recipeId)) {
                throw new GeneralException(ErrorStatus.COMMENT_NOT_MATCH_RECIPE);
            }

            // 대댓글의 대댓글은 금지! (2단계까지만 허용)
            if (parentComment.getParentComment() != null) {
                throw new GeneralException(ErrorStatus.COMMENT_DEPTH_LIMIT_EXCEEDED);
            }
        }

        // 3. 댓글 엔티티 생성
        RecipeComment newComment = RecipeConverter.toRecipeComment(request, recipe, member, parentComment);

        // 4. 댓글 저장
        RecipeComment savedComment = recipeCommentRepository.save(newComment);

        return savedComment.getId();
    }
}
