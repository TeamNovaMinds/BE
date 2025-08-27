package novaminds.gradproj.domain.refrigerator.service.query;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.refrigerator.repository.MemberRefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorSkinQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final MemberRefrigeratorSkinRepository memberRefrigeratorSkinRepository;

    public RefrigeratorResponseDTO.RefrigeratorSkinResponse getRefrigeratorSkin(Long skinId, String memberId) {

        // 냉장고 스킨 조회
        RefrigeratorSkin refrigeratorSkin = refrigeratorSkinRepository.findById(skinId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REFRIGERATOR_SKIN_NOT_FOUND));

        // 해당 냉장고 스킨의 구매 여부 확인
        boolean owned = memberRefrigeratorSkinRepository.existsByMemberLoginIdAndSkinId(memberId, skinId);

        // DTO 변환
        return RefrigeratorConverter.toRefrigeratorSkinResponse(refrigeratorSkin, owned);
    }

    public RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse getRefrigeratorSkins(Long cursorId, String memberId) {

        // 페이지 크기 + 1로 조회하여 다음 페이지 존재 여부 확인
        List<RefrigeratorSkin> skins = refrigeratorSkinRepository.findSkinsWithCursor(cursorId, DEFAULT_PAGE_SIZE + 1);

        // 다음 페이지 존재 여부 판단
        boolean hasNext = skins.size() > DEFAULT_PAGE_SIZE;
        Long nextCursor = null;
        if (hasNext) {
            skins.removeLast(); // 마지막 요소 제거 (페이징에서 다음 요소 유무를 확인하기 위해 가져왔던 +1 추가 데이터 삭제)
            nextCursor = skins.getLast().getId(); // 다음 커서 값 설정
        }

        // DTO List 변환
        var skinResponses = skins.stream()
                .map(skin -> {

                    // 해당 냉장고 스킨의 구매 여부 확인
                    boolean owned = memberRefrigeratorSkinRepository.existsByMemberLoginIdAndSkinId(memberId, skin.getId());
                    // DTO 변환
                    return RefrigeratorConverter.toRefrigeratorSkinListResponse(skin, owned);
                })
                .toList();

        // 무한 스크롤용 반환 DTO 생성
        return RefrigeratorResponseDTO.RefrigeratorSkinsPageResponse.builder()
                .skins(skinResponses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}
