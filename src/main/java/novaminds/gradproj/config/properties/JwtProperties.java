package novaminds.gradproj.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret은 필수입니다")
    private String secret;

    @NotNull(message = "JWT 만료시간은 필수입니다")
    @Positive(message = "JWT 만료시간은 양수여야 합니다")
    private Long expiration;

    @NotNull(message = "Refresh 토큰 만료시간은 필수입니다")
    @Positive(message = "Refresh 토큰 만료시간은 양수여야 합니다")
    private Long refreshExpiration;
}
