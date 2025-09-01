package novaminds.gradproj.domain.recipe.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import novaminds.gradproj.domain.recipe.entity.Difficulty;
import novaminds.gradproj.domain.recipe.entity.RecipeCategory;
import novaminds.gradproj.domain.recipe.entity.RecipeImage;
import novaminds.gradproj.domain.recipe.entity.RecipeIngredient;
import novaminds.gradproj.domain.recipe.entity.RecipeOrder;

public class RecipeResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeListResponse {
        private List<RecipeResponse> recipes; // 레시피 목록
        private int currentPage;
        private boolean hasNext;        // 다음 페이지 존재 여부
        private Long nextCursor;        // 다음 페이지를 위한 커서 (마지막 레시피 ID)
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeResponse {
        private Long recipeId;
        private String title;
        private String mainImageUrl;
        private MemberResponseDTO.AuthorInfo authorInfo; // 작성자 정보 (공용 DTO 활용)
        private Integer cookingTimeMinutes;
        private Difficulty difficulty;
        private int likeCount;
        private int commentCount;

        private boolean likedByMe;      // 현재 사용자가 좋아요를 눌렀는지 여부
        private boolean writtenByMe;    // 현재 사용자가 작성했는지 여부

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeDetailResponse {
        private Long recipeId;
        private String title;
        private String description;
        private RecipeCategory recipeCategory;
        private Integer cookingTimeMinutes;
        private Difficulty difficulty;
        private Integer servings;
        private int likeCount;

        private boolean likedByMe;      // 현재 사용자가 좋아요를 눌렀는지 여부
        private boolean writtenByMe;    // 현재 사용자가 작성했는지 여부

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        private MemberResponseDTO.AuthorInfo authorInfo;
        private List<ImageDTO> recipeImages;
        private List<IngredientDTO> ingredients;
        private List<OrderDTO> orders;
        private CommentPreviewListResponse commentPreview;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreviewListResponse {
        private int totalCommentCount; // 전체 댓글 수
        private List<CommentResponse> previewComments; // 미리 보여줄 댓글 3개
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListResponse {
        private List<CommentResponse> comments;
        private boolean hasNext;
        private Long nextCursor;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long commentId;
        private String content;
        private MemberResponseDTO.AuthorInfo authorInfo; // 작성자 정보 (공용 DTO 활용)

        private boolean writtenByMe; // 현재 사용자가 작성했는지 여부

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        private List<CommentResponse> replies; // 대댓글 목록
    }

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ImageDTO{
		private String imageUrl;
		private boolean isMain;

		public static ImageDTO from(RecipeImage recipeImage) {
			return ImageDTO.builder()
				.imageUrl(recipeImage.getImageUrl())
				.isMain(recipeImage.isMain())
				.build();
		}
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IngredientDTO{

		private String name;
		private String amount;
		private String description;

		public static IngredientDTO from(RecipeIngredient recipeIngredient) {
			return IngredientDTO.builder()
				.name(recipeIngredient.getIngredient().getIngredientName())
				.amount(recipeIngredient.getAmount())
				.build();
		}
	}


	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderDTO{
		private Integer order;
		private String description;
		private String imageUrl;

		public static OrderDTO from(RecipeOrder recipeOrder) {
			return OrderDTO.builder()
				.order(recipeOrder.getOrder())
				.description(recipeOrder.getDescription())
				.imageUrl(recipeOrder.getImageUrl())
				.build();
		}
	}
}
