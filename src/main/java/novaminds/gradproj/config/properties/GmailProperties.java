package novaminds.gradproj.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "gmail")
public class GmailProperties {

    @Valid
    private Mail mail = new Mail();
    @Valid
    private Client client = new Client();
    @Valid
    private Refresh refresh = new Refresh();

    @Getter
    @Setter
    public static class Mail {

        @NotBlank(message = "Gmail SMTP 호스트는 필수입니다")
        private String host;

        @Positive(message = "포트 번호는 양수여야 합니다")
        @Min(value = 1, message = "포트 번호는 1 이상이어야 합니다")
        @Max(value = 65535, message = "포트 번호는 65535 이하여야 합니다")
        private int port;

        @NotBlank(message = "Gmail 사용자명은 필수입니다")
        @Email(message = "올바른 이메일 형식이어야 합니다")
        private String username;
    }

    @Getter
    @Setter
    public static class Client {

        @NotBlank(message = "Gmail OAuth2 Client ID는 필수입니다")
        private String id;

        @NotBlank(message = "Gmail OAuth2 Client Secret은 필수입니다")
        private String secret;
    }

    @Getter
    @Setter
    public static class Refresh {

        @NotBlank(message = "Gmail Refresh Token은 필수입니다")
        private String token;
    }
}
