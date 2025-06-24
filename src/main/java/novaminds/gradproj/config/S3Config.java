package novaminds.gradproj.config;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.config.properties.S3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                s3Properties.getCredentials().getAccessKey(),
                s3Properties.getCredentials().getSecretKey()
        );

        return S3Client.builder()
                .region(Region.of(s3Properties.getRegion().getName()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                s3Properties.getCredentials().getAccessKey(),
                s3Properties.getCredentials().getSecretKey()
        );

        return S3Presigner.builder()
                .region(Region.of(s3Properties.getRegion().getName()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}