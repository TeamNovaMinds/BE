package novaminds.gradproj.domain.recipe.converter;

import novaminds.gradproj.domain.ingredient.entity.Ingredient;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import novaminds.gradproj.domain.recipe.entity.*;
import novaminds.gradproj.domain.recipe.web.dto.RecipeRequestDTO;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;

import java.util.List;
import java.util.Map;

public class RecipeConverter {

    // 레시피 생성 요청을 엔티티로 변환
    public static Recipe toRecipe(Member member, RecipeRequestDTO.CreateRecipeDTO request) {
        return Recipe.builder()
                .author(member)
                .title(request.getTitle())
                .description(request.getDescription())
                .recipeCategory(request.getRecipeCategory())
                .cookingTimeMinutes(request.getCookingTimeMinutes())
                .difficulty(request.getDifficulty())
                .servings(request.getServings())
                .build();
    }

    // 조리 순서 DTO를 엔티티로 변환
    public static RecipeOrder toRecipeOrder(RecipeRequestDTO.RecipeOrderDTO dto, Recipe recipe) {
        return novaminds.gradproj.domain.recipe.entity.RecipeOrder.builder()
                .recipe(recipe)
                .order(dto.getOrder())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    // 이미지 URL과 순서 정보로 레시피 이미지 엔티티 생성
    public static RecipeImage toRecipeImage(String imageUrl, Recipe recipe, int index) {
        return novaminds.gradproj.domain.recipe.entity.RecipeImage.builder()
                .recipe(recipe)
                .imageUrl(imageUrl)
                .imageOrder(index)
                .isMain(index == 0)
                .build();
    }

    // 재료 DTO를 레시피 재료 엔티티로 변환
    public static RecipeIngredient toRecipeIngredient(RecipeRequestDTO.RecipeIngredientDTO dto, Recipe recipe, Ingredient ingredient) {
        return novaminds.gradproj.domain.recipe.entity.RecipeIngredient.builder()
                .recipe(recipe)
                .ingredient(ingredient)
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .build();
    }

    // 댓글 생성 요청을 엔티티로 변환
    public static RecipeComment toRecipeComment(RecipeRequestDTO.CommentCreateRequest request, Recipe recipe, Member author, RecipeComment parentComment) {
        return RecipeComment.builder()
                .author(author)
                .recipe(recipe)
                .content(request.getContent())
                .parentComment(parentComment)
                .build();
    }

    // 레시피를 요약 응답 DTO로 변환
    public static RecipeResponseDTO.RecipeResponse toRecipeSummaryResponse(
            Recipe recipe,
            String mainImageUrl,
            MemberResponseDTO.AuthorInfo authorInfo,
            Integer commentCount,
            boolean likedByMe,
            boolean writtenByMe
    ) {

        return RecipeResponseDTO.RecipeResponse.builder()
                .recipeId(recipe.getId())
                .title(recipe.getTitle())
                .mainImageUrl(mainImageUrl)
                .authorInfo(authorInfo)
                .cookingTimeMinutes(recipe.getCookingTimeMinutes())
                .difficulty(recipe.getDifficulty())
                .likeCount(recipe.getLikes())
                .commentCount(commentCount)
                .likedByMe(likedByMe)
                .writtenByMe(writtenByMe)
                .createdAt(recipe.getCreatedAt())
                .build();
    }

    // 레시피 목록을 페이징 응답 DTO로 변환
    public static RecipeResponseDTO.RecipeListResponse toRecipeListResponse(
            List<RecipeResponseDTO.RecipeResponse> recipes,
            boolean hasNext,
            Long nextCursor
    ) {
        return RecipeResponseDTO.RecipeListResponse.builder()
                .recipes(recipes)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }

    // 레시피 이미지 엔티티 리스트를 응답 DTO로 변환
    public static List<RecipeResponseDTO.RecipeImageDTO> toImageDTOs(List<RecipeImage> recipeImages) {
        return recipeImages.stream()
                .map(RecipeResponseDTO.RecipeImageDTO::from)
                .toList();
    }

    // 레시피 재료 엔티티 리스트를 응답 DTO로 변환
    public static List<RecipeResponseDTO.RecipeIngredientDTO> toIngredientDTOs(List<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream()
                .map(RecipeResponseDTO.RecipeIngredientDTO::from)
                .toList();
    }

    // 조리 순서 엔티티 리스트를 응답 DTO로 변환
    public static List<RecipeResponseDTO.RecipeOrderDTO> toOrderDTOs(List<RecipeOrder> recipeOrders) {
        return recipeOrders.stream()
                .map(RecipeResponseDTO.RecipeOrderDTO::from)
                .toList();
    }

    // 댓글 엔티티 리스트를 응답 DTO로 변환
    public static List<RecipeResponseDTO.CommentResponse> toCommentDTOs(
            List<RecipeComment> comments, 
            RecipeResponseDTO.CommentAuthor commentAuthor,
            String memberId
    ) {
        return comments.stream()
                .map(comment -> toCommentResponseWithAuthors(comment, commentAuthor, memberId))
                .toList();
    }

    // 레시피 상세 응답 DTO 생성
    public static RecipeResponseDTO.RecipeDetailResponse toRecipeDetailResponse(
            Recipe recipe,
            boolean likedByMe,
            boolean writtenByMe,
            List<RecipeResponseDTO.RecipeImageDTO> recipeImageDTOS,
            List<RecipeResponseDTO.RecipeIngredientDTO> recipeIngredientDTOS,
            List<RecipeResponseDTO.RecipeOrderDTO> recipeOrderDTOS,
            List<RecipeResponseDTO.CommentResponse> commentDTOs,
            int totalCommentCount
    ) {

        // 작성자 정보 DTO 생성 (fetch join으로 이미 로딩됨)
        var authorInfo = MemberResponseDTO.AuthorInfo.builder()
                .nickname(recipe.getAuthor().getNickname())
                .profileImageUrl(recipe.getAuthor().getProfileImage())
                .build();

        // 댓글 미리보기 DTO 조립
        var commentPreview = RecipeResponseDTO.CommentPreviewListResponse.builder()
                .totalCommentCount(totalCommentCount)
                .previewComments(commentDTOs)
                .build();

        // 최종 상세 DTO 조립 (모든 컬렉션은 이미 변환된 DTO 사용)
        return RecipeResponseDTO.RecipeDetailResponse.builder()
                .recipeId(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .recipeCategory(recipe.getRecipeCategory())
                .cookingTimeMinutes(recipe.getCookingTimeMinutes())
                .difficulty(recipe.getDifficulty())
                .servings(recipe.getServings())
                .likeCount(recipe.getLikes())
                .likedByMe(likedByMe)
                .writtenByMe(writtenByMe)
                .createdAt(recipe.getCreatedAt())
                .authorInfo(authorInfo)
                .recipeRecipeImageDTOList(recipeImageDTOS)
                .recipeIngredientDTOList(recipeIngredientDTOS)
                .recipeOrderDTOList(recipeOrderDTOS)
                .commentPreview(commentPreview)
                .build();
    }

    // 댓글 목록 응답 DTO 생성
    public static RecipeResponseDTO.CommentListResponse toCommentListResponse(
            List<RecipeResponseDTO.CommentResponse> comments,
            boolean hasNext,
            Long nextCursor) {

        return RecipeResponseDTO.CommentListResponse.builder()
                .comments(comments)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }

    // 댓글 작성자 DTO 생성
    public static RecipeResponseDTO.CommentAuthor createCommentAuthorData(
            Map<Long, String> commentIdToAuthorId,
            Map<String, MemberResponseDTO.AuthorInfo> authorInfos
    ) {
        return RecipeResponseDTO.CommentAuthor.builder()
                .commentIdToAuthorId(commentIdToAuthorId)
                .authorInfos(authorInfos)
                .build();
    }

    // 댓글 엔티티를 응답 DTO로 변환 (미리 조회된 작성자 정보 사용, N+1 방지)
    public static RecipeResponseDTO.CommentResponse toCommentResponseWithAuthors(
            RecipeComment comment,
            RecipeResponseDTO.CommentAuthor commentAuthor,
            String memberId
    ) {
        var commentIdToAuthorId = commentAuthor.getCommentIdToAuthorId();
        var authorInfos = commentAuthor.getAuthorInfos();

        // 1. 대댓글 목록을 DTO로 변환 (미리 조회한 작성자 정보 사용)
        List<RecipeResponseDTO.CommentResponse> replies = comment.getChildren().stream()
                .map(reply -> {
                    // 대댓글의 작성자 ID를 매핑에서 가져옴 (N+1 방지)
                    String replyAuthorId = commentIdToAuthorId.get(reply.getId());
                    MemberResponseDTO.AuthorInfo replyAuthorInfo = authorInfos.get(replyAuthorId);
                    
                    boolean replyWrittenByMe = (memberId != null) && memberId.equals(replyAuthorId);
                    
                    return RecipeResponseDTO.CommentResponse.builder()
                            .commentId(reply.getId())
                            .content(reply.getContent())
                            .authorInfo(replyAuthorInfo)
                            .writtenByMe(replyWrittenByMe)
                            .createdAt(reply.getCreatedAt())
                            .replies(List.of()) // 대댓글의 대댓글은 허용하지 않기 때문에 빈 리스트
                            .build();
                })
                .toList();

        // 2. 부모 댓글의 작성자 정보 가져옴 (N+1 방지)
        String commentAuthorId = commentIdToAuthorId.get(comment.getId());
        MemberResponseDTO.AuthorInfo authorInfo = authorInfos.get(commentAuthorId);
        boolean isWrittenByMe = (memberId != null) && memberId.equals(commentAuthorId);

        // 3. 최종 DTO 반환
        return RecipeResponseDTO.CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .authorInfo(authorInfo)
                .writtenByMe(isWrittenByMe)
                .createdAt(comment.getCreatedAt())
                .replies(replies)
                .build();
    }
}
