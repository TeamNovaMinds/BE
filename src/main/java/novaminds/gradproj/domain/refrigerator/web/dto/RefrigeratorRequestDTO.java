package novaminds.gradproj.domain.refrigerator.web.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RefrigeratorRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class RefrigeratorSkinCreateRequest {

        @NotBlank(message = "스킨 이름은 필수 입력값입니다")
        @Size(max = 50, message = "스킨 이름은 50자 이하여야 합니다")
        private String skinName;

        @Size(max = 1000, message = "설명은 1000자 이하여야 합니다")
        private String description;

        @NotNull(message = "가격은 필수 입력값입니다")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다")
        private Integer price;

        @NotEmpty(message = "이미지는 최소 1개 이상 등록해야 합니다")
        private List<@NotBlank(message = "이미지 URL은 빈 값일 수 없습니다") String> imageUrls;
    }
}
