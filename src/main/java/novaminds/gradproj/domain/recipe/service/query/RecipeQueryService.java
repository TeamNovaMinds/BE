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
import novaminds.gradproj.global.template.CursorPagingHelper;
import novaminds.gradproj.global.template.CursorResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.*;
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

    private final CursorPagingHelper cursorPagingHelper;
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

        // 1. 페이징 로직 처리
        CursorResult<Recipe> pageResult = cursorPagingHelper.getPage(
                // 데이터를 어떻게 가져올지만 정의 (pageSize + 1 만큼)
                (size) -> recipeRepository.findRecipes(category, cursorId, size),
                // 엔티티에서 ID를 어떻게 추출할지만 정의
                Recipe::getId,
                DEFAULT_PAGE_SIZE
        );

        List<Recipe> recipes = pageResult.content();

        // 3. 조회된 레시피가 없으면 빈 페이지 반환
        if (recipes.isEmpty()) {
            return RecipeConverter.toRecipeListResponse(List.of(), pageResult.hasNext(), pageResult.nextCursor());
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
        return RecipeConverter.toRecipeListResponse(recipeSummaries, pageResult.hasNext(), pageResult.nextCursor());
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

        // 1. 페이징 로직 처리
        CursorResult<RecipeComment> pageResult = cursorPagingHelper.getPage(
                (size) -> recipeCommentRepository.findParentComments(recipeId, cursorId, size),
                RecipeComment::getId,
                DEFAULT_PAGE_SIZE
        );

        List<RecipeComment> comments = pageResult.content();

        if (comments.isEmpty()) {
            return RecipeConverter.toCommentListResponse(List.of(), pageResult.hasNext(), pageResult.nextCursor());
        }

        // 3. 댓글 작성자들 배치 조회 (부모 댓글 + 대댓글)
        RecipeResponseDTO.CommentAuthor commentAuthor = getCommentAuthorsData(comments, true);

        // 4. DTO로 변환
        List<RecipeResponseDTO.CommentResponse> commentResponses = 
            RecipeConverter.toCommentDTOs(comments, commentAuthor, memberId);

        // 4. 응답 DTO 변환
        return RecipeConverter.toCommentListResponse(commentResponses, pageResult.hasNext(), pageResult.nextCursor());
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

    /**
     * 냉장고 보관 재료를 기반으로 추천 레시피 목록과 커서 기반 페이징을 위한 {@code nextCursor}, {@code hasNext} 필드를 같이 조회
     *
     * @param member 레시피를 조회하는 회원
     * @param storageType 조회할 보관 타입
     * @param keyword 재료명 검색 키워드 (null이면 전체 조회)
     * @param storedItemId 특정 보관 재료 ID (null이면 모든 보관 재료 대상)
     * @param cursorId 페이징을 위한 커서 (null이면 처음부터 조회)
     *
     * @return 재료별 추천 레시피 그룹 목록과 페이징을 위한 정보를 포함하는 {@code SuggestedRecipeListResponse} 객체
     */
    public RecipeResponseDTO.SuggestedRecipeListResponse suggestRecipesByIngredients(
            Member member,
            StorageType storageType,
            String keyword,
            Long storedItemId,
            Long cursorId
    ) {
        // 1. 사용자 재료 수집
        List<Long> ingredientIds = collectUserIngredientIds(member, storageType, keyword, storedItemId);
        if (ingredientIds.isEmpty()) {
            return RecipeConverter.toSuggestedRecipeListResponse(List.of(), false, null);
        }

        // 2. 페이징 로직 처리
        CursorResult<Recipe> pageResult = cursorPagingHelper.getPage(
                (size) -> recipeRepository.findRecipesByIngredientIds(ingredientIds, cursorId, size),
                Recipe::getId,
                DEFAULT_PAGE_SIZE
        );

        List<Recipe> recipes = pageResult.content();

        if (recipes.isEmpty()) {
            return RecipeConverter.toSuggestedRecipeListResponse(List.of(), pageResult.hasNext(), pageResult.nextCursor());
        }

        // 3. 레시피 인덱스 구축
        RecipeIndexes indexes = buildRecipeIndexes(recipes);

        // 4. 재료별 레시피 그룹 생성
        List<RecipeResponseDTO.SuggestedRecipeGroup> recipeGroups = createRecipeGroups(ingredientIds, indexes);

        // 5. 최종 응답 DTO 생성
        return RecipeConverter.toSuggestedRecipeListResponse(recipeGroups, pageResult.hasNext(), pageResult.nextCursor());
    }

    /**
     * 레시피 ID 목록으로 레시피들의 재료 정보를 배치로 조회하여 Map으로 반환
     *
     * @param recipeIds 조회할 레시피 ID 목록
     * @return {@code recipeId}를 키로, {@code RecipeIngredient} 목록을 값으로 하는 Map
     */
    private Map<Long, List<RecipeIngredient>> getRecipeIngredientsMap(List<Long> recipeIds) {
        List<RecipeIngredient> allRecipeIngredients = recipeIngredientRepository.findByRecipeIdIn(recipeIds);
        return allRecipeIngredients.stream()
                .collect(Collectors.groupingBy(ri -> ri.getRecipe().getId()));
    }

    /**
     * 레시피 ID 목록으로 메인 이미지 URL을 배치로 조회하여 Map으로 반환
     *
     * @param recipeIds 조회할 레시피 ID 목록
     * @return {@code recipeId}를 키로, 메인 이미지 URL을 값으로 하는 Map
     */
    private Map<Long, String> getMainImageMap(List<Long> recipeIds) {
        return recipeImageRepository.findMainImageUrlsByRecipeIds(recipeIds).stream()
                .collect(Collectors.toMap(RecipeMainImage::getRecipeId, RecipeMainImage::getImageUrl));
    }

    /**
     * 특정 재료에 대한 레시피 그룹을 DTO로 변환
     *
     * @param ingredientId 재료 ID
     * @param recipes 해당 재료를 사용하는 레시피 목록
     * @param userIngredientIdSet 사용자가 보유한 재료 ID Set
     * @param ingredientNameMap 재료 ID -> 재료 이름 매핑 Map
     * @param recipeIngredientsMap 레시피 ID -> 재료 목록 매핑 Map
     * @param mainImageMap 레시피 ID -> 메인 이미지 URL 매핑 Map
     * @return 재료별 레시피 그룹 DTO
     */
    private RecipeResponseDTO.SuggestedRecipeGroup createRecipeGroup(
            Long ingredientId,
            List<Recipe> recipes,
            Set<Long> userIngredientIdSet,
            Map<Long, String> ingredientNameMap,
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            Map<Long, String> mainImageMap
    ) {
        // 재료 이름 조회 (매핑에서 찾지 못하면 기본값 사용)
        String ingredientName = ingredientNameMap.getOrDefault(ingredientId, "알 수 없는 재료");

        // 각 레시피를 DTO로 변환
        List<RecipeResponseDTO.SuggestedRecipeResponse> recipeResponses = recipes.stream()
                .map(recipe -> createSuggestedRecipeResponse(
                        recipe, userIngredientIdSet, recipeIngredientsMap, mainImageMap))
                .toList();

        return RecipeConverter.toSuggestedRecipeGroup(ingredientName, recipeResponses);
    }

    /**
     * 레시피를 추천 레시피 응답 DTO로 변환
     *
     * @param recipe 변환할 레시피 엔티티
     * @param userIngredientIdSet 사용자가 보유한 재료 ID Set
     * @param recipeIngredientsMap 레시피 ID -> 재료 목록 매핑 Map
     * @param mainImageMap 레시피 ID -> 메인 이미지 URL 매핑 Map
     * @return 추천 레시피 응답 DTO
     */
    private RecipeResponseDTO.SuggestedRecipeResponse createSuggestedRecipeResponse(
            Recipe recipe,
            Set<Long> userIngredientIdSet,
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            Map<Long, String> mainImageMap
    ) {
        // 레시피의 재료 목록 조회
        List<RecipeIngredient> ingredients = recipeIngredientsMap.getOrDefault(recipe.getId(), List.of());

        // 재료 정보 DTO 변환 (사용자 보유 여부 포함)
        List<RecipeResponseDTO.IngredientInfo> ingredientInfos = ingredients.stream()
                .map(ri -> RecipeConverter.toIngredientInfo(
                        ri.getIngredient().getIngredientName(),
                        ri.getAmount(),
                        userIngredientIdSet.contains(ri.getIngredient().getId()) // 사용자 보유 여부 체크
                ))
                .toList();

        // 메인 이미지 URL 조회
        String mainImageUrl = mainImageMap.get(recipe.getId());
        return RecipeConverter.toSuggestedRecipeResponse(recipe, mainImageUrl, ingredientInfos);
    }

    /**
     * 사용자가 보관 중인 재료 ID 목록을 수집
     *
     * @param member 회원 정보
     * @param storageType 보관 타입
     * @param keyword 검색 키워드
     * @param storedItemId 특정 재료 ID
     * @return 재료 ID 목록
     */
    private List<Long> collectUserIngredientIds(Member member, StorageType storageType, String keyword, Long storedItemId) {
        // 냉장고 조회
        Refrigerator refrigerator = member.getRefrigerator();
        if (refrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        if (storedItemId == null) {
            // 보관 타입별 전체 재료 조회
            List<StoredItem> storedItems = storedItemRepository.findStoredItems(refrigerator.getId(), storageType, keyword);
            return storedItems.stream()
                    .map(storedItem -> storedItem.getIngredient().getId())
                    .distinct()
                    .toList();
        } else {
            // 특정 보관 재료만 조회
            StoredItem storedItem = storedItemRepository
                    .findByIdAndRefrigeratorId(storedItemId, refrigerator.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.STORED_ITEM_NOT_FOUND));
            return List.of(storedItem.getIngredient().getId());
        }
    }

    /**
     * 레시피 인덱스들을 구축하여 반환
     *
     * @param recipes 레시피 목록
     * @return 구축된 인덱스 정보
     */
    private RecipeIndexes buildRecipeIndexes(List<Recipe> recipes) {
        // 배치 조회로 N+1 문제 해결
        List<Long> recipeIds = recipes.stream().map(Recipe::getId).toList();
        Map<Long, List<RecipeIngredient>> recipeIngredientsMap = getRecipeIngredientsMap(recipeIds);
        Map<Long, String> mainImageMap = getMainImageMap(recipeIds);

        // 인덱스 구축: ingredientId -> [Recipe] 매핑 (중복 레시피 제거)
        Map<Long, List<Recipe>> recipesByIngredientId = recipes.stream()
                .flatMap(recipe -> recipeIngredientsMap
                        .getOrDefault(recipe.getId(), List.of()).stream()
                        .map(ri -> Map.entry(ri.getIngredient().getId(), recipe)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.mapping(Map.Entry::getValue, Collectors.toCollection(LinkedHashSet::new)),
                                ArrayList::new
                        )
                ));

        // ingredientId -> ingredientName 매핑 구축 (이름 탐색 성능 최적화 O(1))
        Map<Long, String> ingredientNameMap = recipes.stream()
                .flatMap(recipe -> recipeIngredientsMap.getOrDefault(recipe.getId(), List.of()).stream())
                .collect(Collectors.toMap(
                        ri -> ri.getIngredient().getId(),
                        ri -> ri.getIngredient().getIngredientName(),
                        (existing, replacement) -> existing // 중복 키 발생 시 기존 값 유지
                ));

        return new RecipeIndexes(recipeIngredientsMap, mainImageMap, recipesByIngredientId, ingredientNameMap);
    }

    /**
     * 재료별 레시피 그룹을 생성
     *
     * @param userIngredientIds 사용자 재료 ID 목록
     * @param indexes 레시피 인덱스 정보
     * @return 재료별 레시피 그룹 목록
     */
    private List<RecipeResponseDTO.SuggestedRecipeGroup> createRecipeGroups(
            List<Long> userIngredientIds, RecipeIndexes indexes) {
        
        // Set으로 변환하여 contains 연산 성능 최적화 (O(1))
        Set<Long> userIngredientIdSet = new LinkedHashSet<>(userIngredientIds);

        // 사용자가 보유한 재료만 추출하고 비어있는 그룹 제거
        Map<Long, List<Recipe>> ingredientToRecipesMap = userIngredientIdSet.stream()
                .map(id -> Map.entry(id, indexes.recipesByIngredientId().getOrDefault(id, List.of())))
                .filter(entry -> !entry.getValue().isEmpty()) // 레시피가 없는 재료 제외
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

        // 재료별 레시피 그룹을 DTO로 변환
        return ingredientToRecipesMap.entrySet().stream()
                .map(entry -> createRecipeGroup(
                        entry.getKey(),
                        entry.getValue(),
                        userIngredientIdSet,
                        indexes.ingredientNameMap(),
                        indexes.recipeIngredientsMap(),
                        indexes.mainImageMap()
                ))
                .toList();
    }

    /**
     * 레시피 인덱스 정보를 담는 record
     *
     * @param recipeIngredientsMap 레시피 ID -> 재료 목록 매핑
     * @param mainImageMap 레시피 ID -> 메인 이미지 URL 매핑
     * @param recipesByIngredientId 재료 ID -> 레시피 목록 매핑
     * @param ingredientNameMap 재료 ID -> 재료 이름 매핑
     */
    private record RecipeIndexes(
            Map<Long, List<RecipeIngredient>> recipeIngredientsMap,
            Map<Long, String> mainImageMap,
            Map<Long, List<Recipe>> recipesByIngredientId,
            Map<Long, String> ingredientNameMap
    ) {}
}
