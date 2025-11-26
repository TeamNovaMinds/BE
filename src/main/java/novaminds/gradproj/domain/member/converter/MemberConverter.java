package novaminds.gradproj.domain.member.converter;

import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;

public class MemberConverter {

    public static MemberResponseDTO.AuthorInfo toAuthorInfo(
            String nickname,
            String profileImageUrl,
            boolean following,
            boolean myself
    ) {
        return MemberResponseDTO.AuthorInfo.builder()
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .following(following)
                .myself(myself)
                .build();
    }
}
