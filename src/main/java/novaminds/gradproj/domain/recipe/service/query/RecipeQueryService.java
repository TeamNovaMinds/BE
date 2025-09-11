package novaminds.gradproj.domain.recipe.service.query;

import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.recipe.entity.*;
import novaminds.gradproj.domain.recipe.repository.*;
import novaminds.gradproj.domain.member.service.query.MemberQueryService;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import novaminds.gradproj.domain.recipe.repository.projection.CommentAuthorInfo;
import novaminds.gradproj.domain.recipe.repository.projection.RecipeMainImage;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;
import novaminds.gradproj.domain.recipe.converter.RecipeConverter;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.StorageType;
import novaminds.gradproj.domain.refrigerator.entity.StoredItem;
import novaminds.gradproj.domain.refrigerator.repository.StoredItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeLikeRepository recipeLikeRepository;
    private final RecipeImageRepository recipeImageRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeOrderRepository recipeOrderRepository;
    private final StoredItemRepository storedItemRepository;

    private final MemberQueryService memberQueryService;

    /**
     * 전체 레시피 목록 (or 선택된 카테고리 별 레시피 목록)과 커서 기반 페이징을 위한 {@code nextCursor}, {@code hasNext} 필드를 같이 조회
     * 현재 회원의 레시피 작성 여부, 좋아요 여부 확인 (복수 조회)
     *
     * @param category  조회할 카테고리 - null이면 모든 카테고리 대상
     * @param cursorId  페이징을 위한 커서, null일 경우, 처음부터 조회를 시작
     * @param memberId  레시피를 조회하는 회원의 ID
     *
     * @return 레시피 목록과 페이징을 위한 정보, 현재 회원의 레시피 작성 여부, 좋아요 여부 포함 {@code RecipeListResponse} 객체
     */
    public RecipeResponseDTO.RecipeListResponse getRecipe(String memberId, RecipeCategory category, Long cursorId){

        // 1. 다음 페이지 존재 여부 확인을 위해 요청된 size보다 1개 더 조회
        List<Recipe> recipes = recipeRepository.findRecipes(category, cursorId, DEFAULT_PAGE_SIZE + 1);

        // 2. hasNext와 nextCursor 계산
        boolean hasNext = recipes.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            recipes.removeLast();
            nextCursor = recipes.getLast().getId();
        }

        // 3. 조회된 레시피가 없으면 빈 페이지 반환
        if (recipes.isEmpty()) {
            return RecipeConverter.toRecipeListResponse(List.of(), hasNext, nextCursor);
        }

        // 4. 배치 조회
        List<Long> recipeIds = recipes.stream().map(Recipe::getId).toList();
        final Set<Long> likedRecipeIds = getLikedRecipeIds(memberId, recipes);
        final Map<Long, String> mainImageUrls = recipeImageRepository.findMainImageUrlsByRecipeIds(recipeIds).stream()
                .collect(Collectors.toMap(RecipeMainImage::getRecipeId, RecipeMainImage::getImageUrl));
        final var authorInfoMap = memberQueryService.getAuthorInfoMap(recipes.stream().map(recipe -> recipe.getAuthor().getLoginId()).toList());

        // 5. DTO 리스트로 변환
        var recipeSummaries = recipes.stream()
                .map(recipe -> {
                    boolean isLiked = likedRecipeIds.contains(recipe.getId());
                    boolean isWrittenByMe = isWrittenByUser(memberId, recipe.getAuthor().getLoginId());

                    // 배치 조회된 데이터 사용
                    String mainImageUrl = mainImageUrls.get(recipe.getId());
                    var authorInfo = authorInfoMap.get(recipe.getAuthor().getLoginId());

                    return RecipeConverter.toRecipeSummaryResponse(
                            recipe, mainImageUrl, authorInfo, isLiked, isWrittenByMe);
                })
                .toList();

        // 6. 최종 페이지 응답 DTO로 변환하여 반환
        return RecipeConverter.toRecipeListResponse(recipeSummaries, hasNext, nextCursor);
    }

    /**
     * 특정 레시피 조회 및 현재 회원의 레시피 작성 여부, 좋아요 여부 확인 (단건 조회)
     *
     * @param recipeId  조회하는 레시피 ID
     * @param memberId  레시피를 조회하는 회원의 ID
     *
     * @return 특정 레시피의 정보와 현재 회원의 레시피 작성 여부, 좋아요 여부 포함 {@code RecipeDetailResponse} 객체
     */
    public RecipeResponseDTO.RecipeDetailResponse getRecipeDetail(String memberId, Long recipeId){

        // 1. 레시피 기본 정보 조회 (작성자만 fetch join)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECIPE_NOT_FOUND));

        // 2. 사용자별 동적 정보 조회
        boolean likedByMe = isLikedByUser(memberId, recipeId);
        boolean writtenByMe = isWrittenByUser(memberId, recipe.getAuthor().getLoginId());

        // 3. 각 컬렉션 배치 조회
        List<RecipeImage> images = recipeImageRepository.findByRecipeIdOrderByImageOrder(recipeId);
        List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeIdWithIngredient(recipeId);
        List<RecipeOrder> orders = recipeOrderRepository.findByRecipeIdOrderByOrder(recipeId);
        List<RecipeComment> previewComments = recipeCommentRepository.findTop3ByRecipeIdAndParentCommentIsNullOrderByCreatedAtAsc(recipeId);

        // 4. 댓글 작성자들 배치 조회 (부모 댓글만)
        RecipeResponseDTO.CommentAuthor commentAuthor = getCommentAuthorsData(previewComments, false);

        // 5. 각 컬렉션을 DTO로 변환
        var imageDTOs = RecipeConverter.toImageDTOs(images);
        var ingredientDTOs = RecipeConverter.toIngredientDTOs(ingredients);
        var orderDTOs = RecipeConverter.toOrderDTOs(orders);
        var commentDTOs = RecipeConverter.toParentCommentDTOs(previewComments, commentAuthor, memberId);

        // 6. 응답 DTO 변환
        return RecipeConverter.toRecipeDetailResponse(
                recipe, likedByMe, writtenByMe, imageDTOs, ingredientDTOs, 
                orderDTOs, commentDTOs
        );
    }

    /**
     * 특정 레시피의 모든 댓글 목록과 커서 기반 페이징을 위한 {@code nextCursor}, {@code hasNext} 필드를 같이 조회
     *
     * @param memberId 레시피의 댓글을 조회하는 회원의 ID
     * @param recipeId 조회하는 레시피 ID
     * @param cursorId 페이징을 위한 커서, null일 경우, 처음부터 조회를 시작
     *
     * @return 레시피의 댓글 목록과 페이징을 위한 정보, 현재 회원의 댓글 작성 여부 포함 {@code CommentListResponse} 객체
     */
    public RecipeResponseDTO.CommentListResponse getComments(String memberId, Long recipeId, Long cursorId) {

        // 1. 다음 페이지 확인을 위해 PAGE_SIZE + 1 만큼 댓글 조회
        List<RecipeComment> comments = recipeCommentRepository.findParentComments(recipeId, cursorId, DEFAULT_PAGE_SIZE + 1);

        // 2. 다음 페이지 존재 여부 확인 및 마지막 항목 제거
        boolean hasNext = comments.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            comments.removeLast();
            nextCursor = comments.getLast().getId();
        }

        if (comments.isEmpty()) {
            return RecipeConverter.toCommentListResponse(List.of(), hasNext, nextCursor);
        }

        // 3. 댓글 작성자들 배치 조회 (부모 댓글 + 대댓글)
        RecipeResponseDTO.CommentAuthor commentAuthor = getCommentAuthorsData(comments, true);

        // 4. DTO로 변환
        List<RecipeResponseDTO.CommentResponse> commentResponses = 
            RecipeConverter.toCommentDTOs(comments, commentAuthor, memberId);

        // 4. 응답 DTO 변환
        return RecipeConverter.toCommentListResponse(commentResponses, hasNext, nextCursor);
    }

    /**
     * 사용자가 해당 레시피를 좋아요했는지 확인
     */
    private boolean isLikedByUser(String memberId, Long recipeId) {
        return (memberId != null) && recipeLikeRepository.existsByMemberLoginIdAndRecipeId(memberId, recipeId);
    }

    /**
     * 사용자가 해당 레시피/댓글을 작성했는지 확인
     */
    private boolean isWrittenByUser(String memberId, String authorId) {
        return (memberId != null) && memberId.equals(authorId);
    }

    /**
     * 사용자가 좋아요한 레시피 ID 목록을 일괄 조회
     */
    private Set<Long> getLikedRecipeIds(String memberId, List<Recipe> recipes) {
        if (memberId == null) {
            return Set.of();
        }
        
        List<Long> recipeIds = recipes.stream().map(Recipe::getId).toList();
        return recipeLikeRepository.findLikedRecipeIdsByMemberIdAndRecipeIds(memberId, recipeIds);
    }

    /**
     * 댓글 리스트의 작성자 정보를 배치 조회
     * @param comments 댓글 리스트
     * @param includeReplies 대댓글도 포함할지 여부 (true: 부모+대댓글, false: 부모댓글만)
     * @return Map<댓글 ID, 작성자 ID>과  Map<작성자 ID, 작성자 정보>를 담은 DTO
     */
    private RecipeResponseDTO.CommentAuthor getCommentAuthorsData(List<RecipeComment> comments, boolean includeReplies) {
        if (comments.isEmpty()) {
            return RecipeConverter.createCommentAuthorData(Map.of(), Map.of());
        }

        // 1. 댓글 ID들 수집 (조건에 따라 대댓글 포함/제외)
        List<Long> commentIds;
        if (includeReplies) {
            // 부모 댓글 + 대댓글 모두 수집
            commentIds = comments.stream()
                    .flatMap(comment -> Stream.concat(
                        Stream.of(comment.getId()),
                        comment.getChildren().stream().map(RecipeComment::getId)
                    ))
                    .toList();
        } else {
            // 부모 댓글만 수집
            commentIds = comments.stream()
                    .map(RecipeComment::getId)
                    .toList();
        }

        // 2. 댓글 ID → 작성자 ID 매핑 생성, 배치 조회 사용
        Map<Long, String> commentIdToAuthorId = recipeCommentRepository.findCommentIdAndAuthorId(commentIds).stream()
                .collect(Collectors.toMap(CommentAuthorInfo::getId, CommentAuthorInfo::getAuthorId));

        List<String> authorIds = commentIdToAuthorId.values().stream().toList();

        // 3. 작성자 정보 배치 조회
        Map<String, MemberResponseDTO.AuthorInfo> authorInfos = 
            memberQueryService.getAuthorInfoMap(authorIds);

        return RecipeConverter.createCommentAuthorData(commentIdToAuthorId, authorInfos);
    }

    // 냉장고 재료 기반 레시피 추천 메소드
    public RecipeResponseDTO.SuggestedRecipeListResponse suggestRecipesByIngredients(
            Member member,
            StorageType storageType,
            Long storedItemId,
            Long cursorId
    ) {
        // 1. 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            log.info("Member (ID: {}) does not have a refrigerator.", member.getLoginId());
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        // 2. 사용자가 보관 중인 재료의 Id 가져오기.
        List<Long> ingredientIds;

        if (storedItemId == null) {
            List<StoredItem> storedItems = storedItemRepository.findStoredItems(refrigerator.getId(), storageType, null);
            ingredientIds = storedItems.stream()
                    .map(storedItem -> storedItem.getIngredient().getId())
                    .toList();
        } else {
            StoredItem storedItem = storedItemRepository.findById(storedItemId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND));
            ingredientIds = List.of(storedItem.getIngredient().getId());
        }

        // N+1 해결: 페이징 처리하여 레시피 조회 (좋아요 순 정렬)
        List<Recipe> recipes = recipeRepository.findRecipesByIngredientIds(ingredientIds, cursorId, DEFAULT_PAGE_SIZE + 1);
        
        // 페이징: hasNext와 nextCursor 계산 (기존 패턴과 동일)
        boolean hasNext = recipes.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            recipes.removeLast();
            nextCursor = recipes.getLast().getId();
        }

        if (recipes.isEmpty()) {
            return RecipeConverter.toSuggestedRecipeListResponse(List.of(), hasNext, nextCursor);
        }

        // N+1 해결: 필요한 데이터를 배치로 조회
        List<Long> recipeIds = recipes.stream().map(Recipe::getId).toList();
        Map<Long, List<RecipeIngredient>> recipeIngredientsMap = getRecipeIngredientsMap(recipeIds);
        Map<Long, String> mainImageMap = getMainImageMap(recipeIds);

        // 재료별 레시피 그룹핑 및 DTO 변환
        Map<Long, List<Recipe>> ingredientToRecipesMap = groupRecipesByIngredient(recipes, recipeIngredientsMap, ingredientIds);
        
        List<RecipeResponseDTO.SuggestedRecipeGroup> recipeGroups = ingredientToRecipesMap.entrySet().stream()
                .map(entry -> createRecipeGroup(entry.getKey(), entry.getValue(), ingredientIds, recipeIngredientsMap, mainImageMap))
                .toList();

        return RecipeConverter.toSuggestedRecipeListResponse(recipeGroups, hasNext, nextCursor);
    }

    // N+1 해결: 레시피들의 재료 정보를 배치로 조회
    private Map<Long, List<RecipeIngredient>> getRecipeIngredientsMap(List<Long> recipeIds) {
        List<RecipeIngredient> allRecipeIngredients = recipeIngredientRepository.findByRecipeIdIn(recipeIds);
        return allRecipeIngredients.stream()
                .collect(Collectors.groupingBy(ri -> ri.getRecipe().getId()));
    }

    // N+1 해결: 메인 이미지 URL을 배치로 조회
    private Map<Long, String> getMainImageMap(List<Long> recipeIds) {
        return recipeImageRepository.findMainImageUrlsByRecipeIds(recipeIds).stream()
                .collect(Collectors.toMap(RecipeMainImage::getRecipeId, RecipeMainImage::getImageUrl));
    }

    // 재료별로 레시피 그룹핑 (빈 레시피 그룹은 제외)
    private Map<Long, List<Recipe>> groupRecipesByIngredient(
            List<Recipe> recipes,
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            List<Long> userIngredientIds
    ) {
        return userIngredientIds.stream()
                .collect(Collectors.toMap(
                    ingredientId -> ingredientId,
                    ingredientId -> recipes.stream()
                            .filter(recipe -> recipeIngredientsMap.getOrDefault(recipe.getId(), List.of()).stream()
                                    .anyMatch(ri -> ri.getIngredient().getId().equals(ingredientId)))
                            .toList()
                ))
                .entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())  // 빈 레시피 리스트 제외
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private RecipeResponseDTO.SuggestedRecipeGroup createRecipeGroup(
            Long ingredientId,
            List<Recipe> recipes,
            List<Long> userIngredientIds,
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            Map<Long, String> mainImageMap
    ) {
        // 재료 이름 가져오기
        String ingredientName = recipes.stream()
                .flatMap(recipe -> recipeIngredientsMap.getOrDefault(recipe.getId(), List.of()).stream())
                .filter(ri -> ri.getIngredient().getId().equals(ingredientId))
                .findFirst()
                .map(ri -> ri.getIngredient().getIngredientName())
                .orElse("알 수 없는 재료");
        
        log.info("CreateRecipeGroup - ingredientId: {}, ingredientName: {}, recipes count: {}", 
                ingredientId, ingredientName, recipes.size());

        // 레시피 DTO 변환
        List<RecipeResponseDTO.SuggestedRecipeResponse> recipeResponses = recipes.stream()
                .map(recipe -> createSuggestedRecipeResponse(recipe, userIngredientIds, recipeIngredientsMap, mainImageMap))
                .toList();

        return RecipeConverter.toSuggestedRecipeGroup(ingredientName, recipeResponses);
    }

    private RecipeResponseDTO.SuggestedRecipeResponse createSuggestedRecipeResponse(
            Recipe recipe,
            List<Long> userIngredientIds,
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            Map<Long, String> mainImageMap
    ) {
        // 재료 정보 생성
        List<RecipeIngredient> ingredients = recipeIngredientsMap.getOrDefault(recipe.getId(), List.of());
        List<RecipeResponseDTO.IngredientInfo> ingredientInfos = ingredients.stream()
                .map(ri -> RecipeConverter.toIngredientInfo(
                        ri.getIngredient().getIngredientName(),
                        ri.getAmount(),
                        userIngredientIds.contains(ri.getIngredient().getId())
                ))
                .toList();

        // 메인 이미지 URL
        String mainImageUrl = mainImageMap.get(recipe.getId());

        return RecipeConverter.toSuggestedRecipeResponse(recipe, mainImageUrl, ingredientInfos);
    }
}
