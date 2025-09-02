package novaminds.gradproj.domain.member.converter;

import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;

public class MemberConverter {

    public static MemberResponseDTO.AuthorInfo toAuthorInfo(
            String nickname,
            String profileImageUrl
    ) {
        return MemberResponseDTO.AuthorInfo.builder()
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
