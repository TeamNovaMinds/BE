package novaminds.gradproj.global.s3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.config.properties.S3Properties;
import novaminds.gradproj.global.s3.converter.S3Converter;
import novaminds.gradproj.global.s3.web.dto.S3ResponseDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp", ".gif"
    );

    // 파일 업로드를 위한 presigned URL 생성 (기본: images 디렉토리)
    public S3ResponseDTO.PresignedUrlResponse generatePresignedUploadUrl(String fileName, String contentType) {
        return generatePresignedUploadUrl(fileName, contentType, S3Directory.IMAGES);
    }

    // 디렉토리를 지정할 수 있는 파일 업로드용 presigned URL 생성
    public S3ResponseDTO.PresignedUrlResponse generatePresignedUploadUrl(String fileName, String contentType, S3Directory directory) {

        // Content-Type 검증
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
        }

        // S3에 저장될 파일의 고유 경로(key) 생성
        String key = generateUniqueKey(fileName, directory);

        // 생성된 key를 기반으로 presigned url 생성
        String presignedUrl = generatePresignedUrl(key, contentType);

        // S3에 저장될 image url 생성
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Properties.getS3().getBucket(),
                s3Properties.getRegion().getName(),
                key);

        return S3Converter.toPresignedUrlDTO(presignedUrl, imageUrl);
    }

    // 파일 삭제 - 이건 우리가 직접 수행
    public void deleteImageByUrl(String url) {
        if (url == null || url.isBlank()) {
            return;
        }

        String key = extractKeyFromUrl(url);
        if (key != null) {
            deleteImage(key);
        } else {
            log.warn("S3에서 키 추출 실패 {}", url);
        }
    }

    // url에서 key 추출
    private String extractKeyFromUrl(String url) {
        // 1. URL이 비어있는지 먼저 안전하게 확인
        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            // 2. 표준 URI 클래스로 URL 구조를 분석
            URI uri = URI.create(url);
            String host = uri.getHost();
            String path = uri.getPath(); // 경로 (항상 '/'로 시작)

            // 3. S3 URL이 맞는지 기본적인 검증
            if (host == null || !host.endsWith("amazonaws.com")) {
                log.warn("유효하지 않은 S3 도메인입니다. URL: {}", url);
                return null;
            }

            String expectedBucket = s3Properties.getS3().getBucket();

            // 4. Virtual-hosted 스타일인지 확인 (예: bucket.s3.amazonaws.com)
            if (host.startsWith(expectedBucket + ".")) {
                // 경로의 맨 앞 '/'만 제거하면 바로 key가 됩니다.
                return path.substring(1);
            }

            // 5. Path-style 스타일인지 확인 (예: s3.amazonaws.com/bucket/...)
            String pathPrefix = "/" + expectedBucket + "/";
            if (path.startsWith(pathPrefix)) {
                // "/버킷이름/" 부분을 잘라내면 key가 됩니다.
                return path.substring(pathPrefix.length());
            }

            log.warn("URL에서 버킷 정보를 찾을 수 없거나, 설정된 버킷과 다릅니다. URL: {}", url);
            return null;

        } catch (Exception e) {
            log.warn("S3 URL을 분석하는 데 실패했습니다. URL: {}", url, e);
            return null;
        }
    }

    // 키를 통한 이미지 삭제
    private void deleteImage(String key) {
        // key가 null이거나 비어있으면 예외 발생
        if (key == null || key.trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.S3_FILE_DELETE_FAILED);
        }

        try {
            // S3에 파일 삭제 요청 객체 생성
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(key)
                    .build();

            // S3에 파일 삭제 요청
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", key, e);
            throw new GeneralException(ErrorStatus.S3_FILE_DELETE_FAILED, e.getMessage());
        }
    }

    // S3 URL 목록 검증 (static)
    public static void validateS3Urls(List<String> urls) {
        if (urls != null) {
            urls.forEach(S3Service::validateS3Url);
        }
    }

    // 단일 S3 URL 검증 (static)
    public static void validateS3Url(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_S3_URL);
        }

        // S3에 저장된 것이 맞는지 확인
        if (!url.contains("amazonaws.com/") || !url.startsWith("https://")) {
            throw new GeneralException(ErrorStatus.INVALID_S3_URL);
        }

        // amazonaws.com/ 기준으로 분할하여 key 추출 검증
        String[] parts = url.split("amazonaws.com/");
        if (parts.length <= 1) {
            throw new GeneralException(ErrorStatus.INVALID_S3_URL);
        }

        String key = parts[1];
        
        // 만약 key 뒤에 쿼리 스트링이 있을 경우 제거
        int queryIndex = key.indexOf('?');
        key = queryIndex > 0 ? key.substring(0, queryIndex) : key;
        
        // key가 비어있으면 잘못된 URL
        if (key.trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_S3_URL);
        }
    }

    private String generatePresignedUrl(String key, String contentType) {
        try {
            // S3에 파일을 업로드하기 위한 PutObject 요청 객체 생성 - 저장할 때는 Put 방식으로 저장하기 때문
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(key)
                    .contentType(contentType)
                    .build();

            // Presigned URL 생성을 위한 요청 객체 생성
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(s3Properties.getS3().getPresignedUrlExpiration())
                    .putObjectRequest(putObjectRequest)
                    .build();

            // S3 Presigner를 통해 실제 presigned URL 생성
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            // presigned URL을 문자열로 변환
            String presignedUrl = presignedRequest.url().toString();

            log.info("새로운 key로 presigned url 생성: {}", key);
            return presignedUrl;
        } catch (Exception e) {
            log.error("key를 통한 presigned url 생성 실패: {}", key, e);
            throw new GeneralException(ErrorStatus.S3_PRESIGNED_URL_GENERATION_FAILED, e.getMessage());
        }
    }

    private String generateUniqueKey(String originalFileName, S3Directory directory) {
        // 파일의 확장자 가져오기
        String extension = getFileExtension(originalFileName);

        // 해당 파일의 이름을  UUID로 생성
        String uniqueId = UUID.randomUUID().toString();

        // S3 버킷에 저장될 경로와 파일명을 생성 -> 이게 Key가 됨
        return String.format("%s/%s%s", directory.getPath(), uniqueId, extension);
    }

    private String getFileExtension(String fileName) {
        // 파일의 확장자가 제대로 명시되지 않은 경우
        if (fileName == null || !fileName.contains(".")) {
            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
        }

        // 파일 확장자 추출
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 허용된 확장자만 통과
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new GeneralException(ErrorStatus.INVALID_FILE_TYPE);
        }

        // 파일의 확장자 반환
        return extension;
    }
}
