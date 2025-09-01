package novaminds.gradproj.domain.member.service.query;

import novaminds.gradproj.domain.member.converter.MemberConverter;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    /**
     * 여러 회원의 ID를 받아 AuthorInfo DTO 맵을 반환합니다. (배치 조회)
     * @param memberIds 조회할 회원 ID 목록
     * @return Key: 회원 ID, Value: AuthorInfo DTO
     */
    public Map<String, MemberResponseDTO.AuthorInfo> getAuthorInfoMap(List<String> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return Map.of();
        }

        return memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(
                        Member::getLoginId,
                        MemberConverter::toAuthorInfo
                ));
    }
}
