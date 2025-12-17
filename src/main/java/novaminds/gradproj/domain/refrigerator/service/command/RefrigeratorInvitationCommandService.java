package novaminds.gradproj.domain.refrigerator.service.command;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.apiPayload.code.status.ErrorStatus;
import novaminds.gradproj.apiPayload.exception.GeneralException;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.repository.MemberRepository;
import novaminds.gradproj.domain.refrigerator.converter.RefrigeratorConverter;
import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation.InvitationStatus;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorInvitationRepository;
import novaminds.gradproj.domain.refrigerator.repository.RefrigeratorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefrigeratorInvitationCommandService {

    private final RefrigeratorInvitationRepository invitationRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final MemberRepository memberRepository;
    private final novaminds.gradproj.domain.notification.service.command.NotificationCommandService notificationCommandService;

    /**
     * 냉장고 초대 보내기
     *
     * @param inviter          초대하는 사람
     * @param inviteeNickname  초대받을 사람의 닉네임
     */
    public void sendInvitation(Member inviter, String inviteeNickname) {
        // 초대받을 사람 조회
        Member invitee = memberRepository.findByNickname(inviteeNickname)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 자기 자신을 초대할 수 없음
        if (inviter.getLoginId().equals(invitee.getLoginId())) {
            throw new GeneralException(ErrorStatus.CANNOT_INVITE_SELF);
        }

        // 이미 같은 냉장고를 사용 중인지 확인
        Refrigerator inviterRefrigerator = inviter.getRefrigerator();
        Refrigerator inviteeRefrigerator = invitee.getRefrigerator();

        if (inviterRefrigerator == null) {
            throw new GeneralException(ErrorStatus.REFRIGERATOR_NOT_FOUND);
        }

        if (inviteeRefrigerator != null && inviterRefrigerator.getId().equals(inviteeRefrigerator.getId())) {
            throw new GeneralException(ErrorStatus.ALREADY_IN_SAME_REFRIGERATOR);
        }

        // 이미 대기 중인 초대가 있는지 확인
        invitationRepository.findByInviterAndInviteeAndStatus(inviter, invitee, InvitationStatus.PENDING)
                .ifPresent(invitation -> {
                    throw new GeneralException(ErrorStatus.INVITATION_ALREADY_EXISTS);
                });

        // 초대 생성
        RefrigeratorInvitation invitation = RefrigeratorConverter.toRefrigeratorInvitation(
                inviterRefrigerator, inviter, invitee
        );
        invitationRepository.save(invitation);

        // 알림 발송
        try {
            notificationCommandService.createAndSendNotification(
                    invitee,
                    "냉장고 초대",
                    inviter.getNickname() + "님이 회원님을 공유 냉장고에 초대했습니다.",
                    "/refrigerator/invitation/" + invitation.getId(),
                    novaminds.gradproj.domain.notification.entity.NotificationType.REFRIGERATOR_INVITATION
            );
        } catch (Exception e) {
            // 알림 발송 실패해도 초대는 정상 처리
        }
    }

    /**
     * 냉장고 초대 수락
     *
     * @param invitee      초대받은 사람
     * @param invitationId 초대 ID
     */
    public void acceptInvitation(Member invitee, Long invitationId) {
        // 초대 조회
        RefrigeratorInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVITATION_NOT_FOUND));

        // 초대받은 사람이 맞는지 확인
        if (!invitation.getInvitee().getLoginId().equals(invitee.getLoginId())) {
            throw new GeneralException(ErrorStatus.INVITATION_NOT_AUTHORIZED);
        }

        // 이미 처리된 초대인지 확인
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new GeneralException(ErrorStatus.INVITATION_ALREADY_PROCESSED);
        }

        // 초대 수락
        invitation.accept();

        // 냉장고 변경
        Refrigerator inviterRefrigerator = invitation.getRefrigerator();
        Refrigerator oldRefrigerator = invitee.getRefrigerator();
        invitee.setRefrigerator(inviterRefrigerator);

        // 기존 냉장고에 아무도 남지 않았으면 삭제
        if (oldRefrigerator != null && oldRefrigerator.getMemberList().isEmpty()) {
            refrigeratorRepository.delete(oldRefrigerator);
        }
    }

    /**
     * 냉장고 초대 거절
     *
     * @param invitee      초대받은 사람
     * @param invitationId 초대 ID
     */
    public void rejectInvitation(Member invitee, Long invitationId) {
        // 초대 조회
        RefrigeratorInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVITATION_NOT_FOUND));

        // 초대받은 사람이 맞는지 확인
        if (!invitation.getInvitee().getLoginId().equals(invitee.getLoginId())) {
            throw new GeneralException(ErrorStatus.INVITATION_NOT_AUTHORIZED);
        }

        // 이미 처리된 초대인지 확인
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new GeneralException(ErrorStatus.INVITATION_ALREADY_PROCESSED);
        }

        // 초대 거절
        invitation.reject();
    }

    /**
     * 냉장고 초대 취소
     *
     * @param inviter      초대한 사람
     * @param invitationId 초대 ID
     */
    public void cancelInvitation(Member inviter, Long invitationId) {
        // 초대 조회
        RefrigeratorInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVITATION_NOT_FOUND));

        // 초대한 사람이 맞는지 확인
        if (!invitation.getInviter().getLoginId().equals(inviter.getLoginId())) {
            throw new GeneralException(ErrorStatus.INVITATION_NOT_AUTHORIZED);
        }

        // 이미 처리된 초대인지 확인
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new GeneralException(ErrorStatus.INVITATION_ALREADY_PROCESSED);
        }

        // 초대 취소
        invitation.cancel();
    }
}