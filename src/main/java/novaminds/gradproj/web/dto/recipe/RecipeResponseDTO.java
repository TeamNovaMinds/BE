package novaminds.gradproj.web.dto.recipe;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.Difficulty;
import novaminds.gradproj.domain.Recipe.RecipeCategory;
import novaminds.gradproj.domain.Recipe.RecipeComment;
import novaminds.gradproj.domain.Recipe.RecipeImage;
import novaminds.gradproj.domain.Recipe.RecipeIngredient;
import novaminds.gradproj.domain.Recipe.RecipeOrder;
import novaminds.gradproj.domain.user.User;

public class RecipeResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeResultDTO {
		private Long recipeId;
	}

	//레시피 상세보기 dto
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RecipeDetailDTO{
		private Long recipeId;
		private String title;
		private String description;
		private RecipeCategory recipeCategory;
		private Integer cookingTimeMinutes;
		private Difficulty difficulty;
		private Integer servings;
		private LocalDateTime createdAt;
		private Integer likeCount;
		private CommentPreviewDTO commentPreview; // 댓글 미리보기 정보
		private AuthorDTO author;
		private List<ImageDTO> recipeImages;
		private List<IngredientDTO> ingredients;
		private List<OrderDTO> orders;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AuthorDTO{
		private String nickname;
		private String profileImageUrl;

		public static AuthorDTO from(User user) {
			return AuthorDTO.builder()
				.nickname(user.getNickname())
				.profileImageUrl(user.getProfileImage())
				.build();
		}
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
				.imageUrl(recipeOrder.getImgUrl())
				.build();
		}
	}

	//전체 댓글 더보기 할 때 사용할 dto
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CommentDTO{
		private Long commentId;
		private String content;
		private LocalDateTime createdAt;
		private AuthorDTO author;
		private List<CommentDTO> children;

		public static CommentDTO from(RecipeComment comment) {
			return CommentDTO.builder()
				.commentId(comment.getId())
				.content(comment.getContent())
				.createdAt(comment.getCreatedAt())
				.author(AuthorDTO.from(comment.getAuthor()))
				.children(comment.getChildren().stream()
					.map(CommentDTO::from)
					.collect(Collectors.toList()))
				.build();
		}
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CommentPreviewDTO{
		private long totalCount;
		private List<CommentDTO> previewComments;
	}

	//category별 레시피 조회
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ListByCategoryDTO{
		private Long recipeId;
		private String title;
		private String mainImageUrl;
		private String authorNickname;
		private String authorProfileImg;
		private Integer cookingTimeMinutes;
		private Difficulty difficulty;
		private Integer servings;
		private Long likeCount;
		private Long commentCount;
		private LocalDateTime createdAt;

	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LikeDTO {
		private Long recipeId;
		private boolean isLiked; // 현재 나의 좋아요 상태
		private int likeCount;   // 변경 후 총 좋아요 개수
	}
}
