package novaminds.gradproj.domain.userdevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novaminds.gradproj.domain.userdevice.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

}
