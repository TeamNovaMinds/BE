package novaminds.gradproj.domain.refrigerator.service.command;

import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkinImage;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinImageRepository;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorSkinRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorCommandService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorSkinRepository refrigeratorSkinRepository;
    private final RefrigeratorSkinImageRepository refrigeratorSkinImageRepository;
    private final MemberRepository memberRepository;

    public void createRefrigerator(Member member) {

        // 냉장고 생성
        Refrigerator refrigerator = Refrigerator.builder()
                .member(member)
                .build();

        // 냉장고 저장
        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);

        // 회원과 냉장고 연관관계 생성
        member.setRefrigerator(savedRefrigerator);
    }

    public Long registerRefrigeratorSkin(String memberId, RefrigeratorRequestDTO.RefrigeratorSkinCreateRequest request) {

        // 관리자인지 권한 확인
        if (!memberRepository.isAdminByLoginId(memberId)) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_ADMIN);
        }

        // 냉장고 스킨 생성
        RefrigeratorSkin refrigeratorSkin = RefrigeratorSkin.builder()
                .skinName(request.getSkinName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        // 저장한 스킨 가져오기
        RefrigeratorSkin savedRefrigeratorSkin = refrigeratorSkinRepository.save(refrigeratorSkin);

        // 스킨 이미지 url 가져오기
        List<String> imageUrls = request.getImageUrls();

        // 가져온 이미지 url로 스킨 이미지 엔티티 생성
        List<RefrigeratorSkinImage> skinImages = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> RefrigeratorSkinImage.builder()
                        .refrigeratorSkin(savedRefrigeratorSkin)
                        .imageUrl(imageUrls.get(i))
                        .imageOrder(i)
                        .build())
                .toList();

        // 생성된 스킨 이미지 엔티티 모두 저장
        refrigeratorSkinImageRepository.saveAll(skinImages);

        // 스킨 이미지 엔티티와 스킨과의 연관관계 설정
        skinImages.forEach(savedRefrigeratorSkin::addRefrigeratorSkinImage);

        return savedRefrigeratorSkin.getId();
    }
}
