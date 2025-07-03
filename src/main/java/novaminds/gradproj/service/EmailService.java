package novaminds.gradproj.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.config.properties.GmailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final GmailProperties gmailProperties;

    public void sendPasswordResetEmail(String email, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 이메일 기본 정보 설정
            helper.setFrom(gmailProperties.getMail().getUsername(), "Just Fridge");
            helper.setTo(email);
            helper.setSubject("[Just Fridge] 비밀번호 재설정 인증 코드");

            // HTML 이메일 내용 생성
            String htmlContent = createPasswordResetEmailContent(token);
            helper.setText(htmlContent, true); // true = HTML 형식

            // 이메일 발송
            emailSender.send(message);

            log.info("✅ 비밀번호 재설정 이메일 발송 완료 - 수신자: {}", email);

        } catch (MessagingException e) {
            log.error("❌ 비밀번호 재설정 이메일 발송 실패 - 수신자: {}, 오류: {}", email, e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.", e);
        } catch (Exception e) {
            log.error("❌ 예상하지 못한 이메일 발송 오류 - 수신자: {}, 오류: {}", email, e.getMessage(), e);
            throw new RuntimeException("이메일 발송 중 오류가 발생했습니다.", e);
        }
    }

    private String createPasswordResetEmailContent(String token) {
        String template = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>비밀번호 재설정</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Helvetica Neue', Arial, sans-serif; background-color: #f8f9fa;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                    
                    <!-- 헤더 -->
                    <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: 300;">JustFridge</h1>
                        <p style="color: #e8eaff; margin: 10px 0 0 0; font-size: 16px;">비밀번호 재설정</p>
                    </div>
                    
                    <!-- 메인 콘텐츠 -->
                    <div style="padding: 40px 30px;">
                        <h2 style="color: #333333; margin: 0 0 20px 0; font-size: 24px; font-weight: 400;">안녕하세요!</h2>
                        
                        <p style="color: #666666; line-height: 1.6; margin: 0 0 30px 0; font-size: 16px;">
                            비밀번호 재설정을 위한 인증 코드를 보내드립니다.<br>
                            아래 6자리 코드를 입력해주세요.
                        </p>
                        
                        <!-- 인증 코드 -->
                        <div style="text-align: center; margin: 30px 0;">
                            <div style="display: inline-block; background-color: #f8f9fa; border: 2px dashed #667eea; border-radius: 8px; padding: 20px 30px;">
                                <div style="font-size: 32px; font-weight: bold; color: #667eea; letter-spacing: 4px; font-family: 'Courier New', monospace;">
                                    {{TOKEN}}
                                </div>
                            </div>
                        </div>
                        
                        <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 6px; padding: 15px; margin: 30px 0;">
                            <p style="color: #856404; margin: 0; font-size: 14px;">
                                ⚠️ <strong>보안 알림:</strong><br>
                                • 이 코드는 24시간 후 만료됩니다<br>
                                • 본인이 요청하지 않았다면 이 이메일을 무시하세요<br>
                                • 코드를 타인과 공유하지 마세요
                            </p>
                        </div>
                    </div>
                    
                    <!-- 푸터 -->
                    <div style="background-color: #f8f9fa; padding: 20px 30px; text-align: center; border-top: 1px solid #e9ecef;">
                        <p style="color: #999999; margin: 0; font-size: 12px;">
                            © 2025 Just Fridge. All rights reserved.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """;

        return template.replace("{{TOKEN}}", token);
    }
}