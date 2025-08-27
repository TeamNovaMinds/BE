package novaminds.gradproj.domain.refrigerator.service.query;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;

    public RefrigeratorResponseDTO.RefrigeratorSkinResponse getRefrigeratorSkin(Long skinId) {

        // 냉장고 스킨 조회
        RefrigeratorSkin refrigeratorSkin = refrigeratorSkinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_NOT_FOUND));

        // DTO 변환
        return RefrigeratorConverter.toRefrigeratorSkinResponse(refrigeratorSkin);
    }

    public RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse getRefrigeratorSkins(Long cursorId) {

        // 페이지 크기 + 1로 조회하여 다음 페이지 존재 여부 확인
        List<RefrigeratorSkin> skins = refrigeratorSkinRepository.findSkinsWithCursor(cursorId, DEFAULT_PAGE_SIZE + 1);
        
        // 다음 페이지 존재 여부 판단
        boolean hasNext = skins.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            skins.removeLast(); // 마지막 요소 제거 (페이징에서 다음 요소 유무를 확인하기 위해 가져왔던 +1 추가 데이터 삭제)
            nextCursor = skins.getLast().getId(); // 다음 커서 값 설정
        }
        
        // 엔티티를 DTO로 변환
        var skinResponses = skins.stream()
                .map(RefrigeratorConverter::toRefrigeratorSkinListResponse)
                .toList();
        
        // 페이징 응답 객체 생성
        return RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse.builder()
                .skins(skinResponses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
