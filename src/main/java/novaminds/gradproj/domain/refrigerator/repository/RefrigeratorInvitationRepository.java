package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation;
import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorInvitation.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefrigeratorInvitationRepository extends JpaRepository<RefrigeratorInvitation, Long> {

    Optional<RefrigeratorInvitation> findByInviterAndInviteeAndStatus(
            Member inviter,
            Member invitee,
            InvitationStatus status
    );

    List<RefrigeratorInvitation> findByInviteeAndStatusOrderByCreatedAtDesc(
            Member invitee,
            InvitationStatus status
    );

    List<RefrigeratorInvitation> findByInviterAndStatusOrderByCreatedAtDesc(
            Member inviter,
            InvitationStatus status
    );

    List<RefrigeratorInvitation> findByInviterAndInviteeInAndStatus(
            Member inviter,
            List<Member> invitees,
            InvitationStatus status
    );
}