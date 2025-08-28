package novaminds.gradproj.global.s3.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import novaminds.gradproj.global.s3.web.dto.S3ResponseDTO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class S3Converter {

    public static S3ResponseDTO.PresignedUrlResponse toPresignedUrlDTO(
            String presignedUrl,
            String imageUrl
    ) {
        return S3ResponseDTO.PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .imageUrl(imageUrl)
                .build();
    }
}
