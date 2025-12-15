package novaminds.gradproj.domain.refrigerator.service.query;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation.InvitationStatus;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorInvitationRepository;
import novaminds.gradproj.domain.refrigerator.web.dto.RefrigeratorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefrigeratorInvitationQueryService {

    private final RefrigeratorInvitationRepository invitationRepository;

    /**
     * 받은 초대 목록 조회 (대기 중인 초대만)
     *
     * @param member 현재 로그인한 사용자
     * @return 받은 초대 목록
     */
    public RefrigeratorResponseDTO.InvitationListResponse getReceivedInvitations(Member member) {
        List<RefrigeratorInvitation> invitations = invitationRepository
                .findByInviteeAndStatusOrderByCreatedAtDesc(member, InvitationStatus.PENDING);

        return RefrigeratorConverter.toInvitationListResponse(invitations);
    }

    /**
     * 보낸 초대 목록 조회 (대기 중인 초대만)
     *
     * @param member 현재 로그인한 사용자
     * @return 보낸 초대 목록
     */
    public RefrigeratorResponseDTO.InvitationListResponse getSentInvitations(Member member) {
        List<RefrigeratorInvitation> invitations = invitationRepository
                .findByInviterAndStatusOrderByCreatedAtDesc(member, InvitationStatus.PENDING);

        return RefrigeratorConverter.toInvitationListResponse(invitations);
    }
}