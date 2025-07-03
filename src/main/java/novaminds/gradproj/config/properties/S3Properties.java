package novaminds.gradproj.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

    private Credentials credentials = new Credentials();
    private Region region = new Region();
    private S3 s3 = new S3();

    @Getter
    @Setter
    public static class Credentials {
        @NotBlank(message = "AWS Access Key는 필수입니다")
        private String accessKey;

        @NotBlank(message = "AWS Secret Key는 필수입니다")
        private String secretKey;
    }

    @Getter
    @Setter
    public static class Region {
        @NotBlank(message = "AWS Region은 필수입니다")
        private String name;
    }

    @Getter
    @Setter
    public static class S3{
        @NotBlank(message = "S3 Bucket 이름은 필수입니다")
        private String bucket;
    }
}
