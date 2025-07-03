package novaminds.gradproj.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.config.properties.GmailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GmailOauth2Config {

    private final GmailProperties gmailProperties;
    private String accessToken;
    private long expirationTimeMillis;

    @Bean
    @Primary // 기존 JavaMailSender 대신 이 빈을 우선 사용
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(gmailProperties.getMail().getHost());
        mailSender.setPort(gmailProperties.getMail().getPort());
        mailSender.setUsername(gmailProperties.getMail().getUsername());
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.trust", gmailProperties.getMail().getHost());
        props.put("mail.debug", true);

        // OAuth2 설정
        props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
        props.put("mail.smtp.sasl.enable", "true");
        props.put("mail.smtp.auth.xoauth2.disable", "false");

        // OAuth2 인증기 설정
        mailSender.setSession(Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 토큰 만료 시 갱신
                if (System.currentTimeMillis() >= expirationTimeMillis) {
                    refreshAccessToken();
                }
                return new PasswordAuthentication(gmailProperties.getMail().getUsername(), accessToken);
            }
        }));

        return mailSender;
    }

    private void refreshAccessToken() {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    gmailProperties.getRefresh().getToken(),
                    gmailProperties.getClient().getId(),
                    gmailProperties.getClient().getSecret()
            ).execute();

            accessToken = response.getAccessToken();
            expirationTimeMillis = System.currentTimeMillis() + (response.getExpiresInSeconds() * 1000);

            log.info("Gmail 액세스 토큰 갱신 완료. 만료 시간: {}",
                    new java.util.Date(expirationTimeMillis));
        } catch (IOException e) {
            log.error("Gmail 액세스 토큰 갱신 실패: {}", e.getMessage(), e);
        }
    }
}