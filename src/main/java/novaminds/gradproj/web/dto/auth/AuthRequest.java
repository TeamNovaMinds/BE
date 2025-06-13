package novaminds.gradproj.web.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.Recipe.RecipeCategory;

import java.util.List;

public class AuthRequest {

    @Getter
    @NoArgsConstructor
    public static class LoginRequest {

        @NotBlank(message = "이메일은 필수 입력값입니다")
        @Email(message = "유효한 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])(?!.*\\s)[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "비밀번호는 8-16자 사이, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 하며 공백은 포함할 수 없습니다.")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class SignupRequest {

        @NotBlank(message = "이메일은 필수 입력값입니다")
        @Email(message = "유효한 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])(?!.*\\s)[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "비밀번호는 8-16자 사이, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 하며 공백은 포함할 수 없습니다.")
        private String password;

        @NotBlank(message = "이름은 필수 입력값입니다")
        @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class AdditionalInfoNicknameRequest {

        @NotBlank(message = "닉네임은 필수 입력값입니다")
        @Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9_-]+$", message = "닉네임은 한글, 영문, 숫자, 언더스코어(_), 하이픈(-)만 사용할 수 있습니다")
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    public static class AdditionalInfoInterestRequest {
        @NotNull(message = "관심 카테고리는 필수 입력값입니다")
        @Size(min = 1, max = 3, message = "관심 카테고리는 1개 이상 3개 이하로 선택해야 합니다")
        private List<RecipeCategory> interestCategories;
    }

    @Getter
    @NoArgsConstructor
    public static class RefreshTokenRequest {
        // RefreshToken은 Cookie에서 자동으로 가져오므로 별도 필드 불필요
    }
}