package novaminds.gradproj;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
		exclude = {
                RedisRepositoriesAutoConfiguration.class,
				S3AutoConfiguration.class
		}
)
@EnableJpaAuditing
public class GradprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradprojApplication.class, args);
	}

}
