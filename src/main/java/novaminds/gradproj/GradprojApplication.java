package novaminds.gradproj;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
		exclude = {
                RedisRepositoriesAutoConfiguration.class,
				S3AutoConfiguration.class
		}
)
@EnableJpaAuditing
@EnableRetry
@EnableScheduling
public class GradprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradprojApplication.class, args);
	}

}
