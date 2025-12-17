package novaminds.gradproj.domain.userdevice.repository;

import novaminds.gradproj.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.userdevice.entity.UserDevice;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

	List<UserDevice> findByMemberAndIsActiveTrue(Member member);

	Optional<UserDevice> findByMemberAndDeviceId(Member member, String deviceId);

	List<UserDevice> findByMember(Member member);
}
