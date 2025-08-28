package novaminds.gradproj.domain.refrigerator.service.command;

import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorCommandService {

    private final RefrigeratorRepository refrigeratorRepository;

    /**
     * 회원에게 새로운 냉장고를 생성하고 연결하고 저장
     *
     * @param member 새로 생성된 냉장고와 연결할 회원 엔티티
     */
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
}
