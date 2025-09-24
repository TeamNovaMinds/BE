package novaminds.gradproj;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication(
		exclude = {
                RedisRepositoriesAutoConfiguration.class,
				S3AutoConfiguration.class,
                ReactiveElasticsearchRepositoriesAutoConfiguration.class
		}
)
@EnableJpaAuditing
@EnableRetry
@EnableJpaRepositories(
        basePackages = "novaminds.gradproj.domain"
)
@EnableElasticsearchRepositories(basePackages = "novaminds.gradproj.global.search.repository")
public class GradprojApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradprojApplication.class, args);
	}

}
