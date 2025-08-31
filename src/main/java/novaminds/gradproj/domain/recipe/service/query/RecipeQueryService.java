package novaminds.gradproj.domain.recipe.service.query;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.recipe.entity.Recipe;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.recipe.entity.RecipeComment;
import novaminds.gradproj.domain.recipe.entity.RecipeImage;
import novaminds.gradproj.domain.recipe.repository.RecipeCommentRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeLikeRepository;
import novaminds.gradproj.domain.recipe.repository.RecipeRepository;
import novaminds.gradproj.domain.recipe.web.dto.RecipeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeQueryService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final RecipeLikeRepository recipeLikeRepository;

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
            throw new GeneralException(ErrorStatus.RECIPE_NOT_FOUND);
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
                    .orElse(recipe.getRecipeImages().isEmpty() ? null : recipe.getRecipeImages().getFirst().getImageUrl());

            long likeCount = recipeLikeRepository.countByRecipeId(recipe.getId());
            long commentCount = recipeCommentRepository.countByRecipeId(recipe.getId());

            return RecipeResponseDTO.ListByCategoryDTO.builder()
                    .recipeId(recipe.getId())
                    .title(recipe.getTitle())
                    .recipeCategory(recipe.getRecipeCategory())
                    .mainImageUrl(mainImageUrl)
                    .authorNickname(recipe.getAuthor().getNickname())
                    .authorProfileImg(recipe.getAuthor().getProfileImage())
                    .cookingTimeMinutes(recipe.getCookingTimeMinutes())
                    .difficulty(recipe.getDifficulty())
                    .servings(recipe.getServings())
                    .likeCount(likeCount)
                    .commentCount(commentCount)
                    .createdAt(recipe.getCreatedAt())
                    .build();
        });
    }
}
