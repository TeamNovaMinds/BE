package novaminds.gradproj.domain.member.converter;

import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;

public class MemberConverter {

    public static MemberResponseDTO.AuthorInfo toAuthorInfo(Member member) {
        return MemberResponseDTO.AuthorInfo.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                .build();
    }
}
