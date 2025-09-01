package novaminds.gradproj.domain.recipe.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import novaminds.gradproj.domain.recipe.entity.*;

public class RecipeResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeListResponse {
        private List<RecipeResponse> recipes; // 레시피 목록
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
        private List<RecipeImageDTO> recipeImageDTOs;
        private List<RecipeIngredientDTO> recipeIngredientDTOs;
        private List<RecipeOrderDTO> recipeOrderDTOs;
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
	public static class RecipeImageDTO {
		private String imageUrl;
		private boolean isMain;

		public static RecipeImageDTO from(novaminds.gradproj.domain.recipe.entity.RecipeImage recipeImage) {
			return RecipeImageDTO.builder()
				.imageUrl(recipeImage.getImageUrl())
				.isMain(recipeImage.isMain())
				.build();
		}
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeIngredientDTO {

		private String name;
		private String amount;
		private String description;

		public static RecipeIngredientDTO from(novaminds.gradproj.domain.recipe.entity.RecipeIngredient recipeIngredient) {
			return RecipeIngredientDTO.builder()
				.name(recipeIngredient.getIngredient().getIngredientName())
				.amount(recipeIngredient.getAmount())
				.build();
		}
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeOrderDTO {
		private Integer order;
		private String description;
		private String imageUrl;

		public static RecipeOrderDTO from(novaminds.gradproj.domain.recipe.entity.RecipeOrder recipeOrder) {
			return RecipeOrderDTO.builder()
				.order(recipeOrder.getOrder())
				.description(recipeOrder.getDescription())
				.imageUrl(recipeOrder.getImageUrl())
				.build();
		}
	}

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
	public static class CommentAuthor {
		private Map<Long, String> commentIdToAuthorId;
		private Map<String, MemberResponseDTO.AuthorInfo> authorInfos;
	}
}
