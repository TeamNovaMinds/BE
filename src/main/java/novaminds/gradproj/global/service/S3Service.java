package novaminds.gradproj.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.config.properties.S3Properties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    /**
     * S3에 파일 업로드
     * @param multipartFile 업로드할 파일
     * @param dirName 저장할 디렉토리명 (예: profile-images, recipe-images)
     * @return 업로드된 파일의 URL
     */
    public String uploadFile(MultipartFile multipartFile, String dirName) {
        // 파일 유효성 검사
        validateFile(multipartFile);

        // 파일명 생성 (UUID + 원본 파일명)
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = dirName + "/" + UUID.randomUUID() + extension;

        log.info("🔄 [S3 업로드] 시작 - 파일명: {}, 크기: {} bytes", fileName, multipartFile.getSize());

        try {
            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            // 업로드된 파일의 URL 반환
            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    s3Properties.getS3().getBucket(), s3Properties.getRegion().getName(), fileName);

            log.info("✅ [S3 업로드] 완료 - URL: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            log.error("❌ [S3 업로드] 실패 - {}", e.getMessage());
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * S3에서 파일 삭제
     * @param fileUrl 삭제할 파일의 URL
     */
    public void deleteFile(String fileUrl) {
        try {
            // URL에서 파일명 추출
            String fileName = extractFileNameFromUrl(fileUrl);

            log.info("🔄 [S3 삭제] 시작 - 파일명: {}", fileName);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            log.info("✅ [S3 삭제] 완료 - 파일명: {}", fileName);

        } catch (Exception e) {
            log.error("❌ [S3 삭제] 실패 - {}", e.getMessage());
            throw new RuntimeException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 파일 유효성 검사
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 이미지 파일만 허용
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        // 파일 크기 제한 (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }
    }

    /**
     * URL에서 파일명 추출
     */
    private String extractFileNameFromUrl(String fileUrl) {
        String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
        return decodedUrl.substring(decodedUrl.lastIndexOf(".com/") + 5);
    }
}