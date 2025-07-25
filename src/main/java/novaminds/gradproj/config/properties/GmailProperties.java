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
@ConfigurationProperties(prefix = "mail")
public class GmailProperties {

    @NotBlank(message = "Gmail SMTP 호스트는 필수입니다")
    private String host;

    @Positive(message = "포트 번호는 양수여야 합니다")
    private int port;

    @NotBlank(message = "Gmail 사용자명은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    private String username;

    @NotBlank(message = "Gmail 앱 비밀번호는 필수입니다")
    private String password;
}
