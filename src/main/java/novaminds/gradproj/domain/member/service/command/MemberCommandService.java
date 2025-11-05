package novaminds.gradproj.domain.member.service.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.web.dto.MemberRequestDTO;
import novaminds.gradproj.domain.member.web.dto.MemberResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandService {

    @CacheEvict(value = "memberInfo", key = "#member.loginId")
    public MemberResponseDTO.UpdateProfileImageResponse updateProfileImage(
            Member member,
            MemberRequestDTO.UpdateProfileImageRequest request
    ) {
        member.updateProfileImage(request.getProfileImgUrl());
        return MemberResponseDTO.UpdateProfileImageResponse.from(member);
    }
}
