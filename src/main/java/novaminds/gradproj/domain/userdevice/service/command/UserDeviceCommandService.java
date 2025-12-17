package novaminds.gradproj.domain.userdevice.service.command;

import lombok.RequiredArgsConstructor;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.userdevice.converter.UserDeviceConverter;
import novaminds.gradproj.domain.userdevice.entity.UserDevice;
import novaminds.gradproj.domain.userdevice.repository.UserDeviceRepository;
import novaminds.gradproj.domain.userdevice.web.dto.UserDeviceRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDeviceCommandService {

	private final UserDeviceRepository userDeviceRepository;

	/**
	 * 디바이스 등록 또는 토큰 업데이트
	 * 같은 디바이스 ID가 이미 있으면 토큰만 업데이트
	 */
	public UserDevice registerOrUpdateDevice(Member member, UserDeviceRequestDTO.RegisterDeviceRequest request) {
		Optional<UserDevice> existingDevice = userDeviceRepository.findByMemberAndDeviceId(member, request.getDeviceId());

		if (existingDevice.isPresent()) {
			// 기존 디바이스가 있으면 토큰만 업데이트
			UserDevice device = existingDevice.get();
			updateDeviceToken(device, request.getExpoPushToken());
			return device;
		} else {
			// 새로운 디바이스 등록
			UserDevice newDevice = UserDeviceConverter.toUserDevice(member, request);
			return userDeviceRepository.save(newDevice);
		}
	}

	/**
	 * 디바이스의 Expo Push Token 업데이트
	 */
	private void updateDeviceToken(UserDevice device, String newToken) {
		// UserDevice 엔티티에 setter가 없으므로 리플렉션 또는 빌더 패턴 사용
		// 일단 새로운 엔티티를 만들어서 업데이트
		UserDevice updatedDevice = UserDevice.builder()
				.id(device.getId())
				.member(device.getMember())
				.deviceType(device.getDeviceType())
				.deviceId(device.getDeviceId())
				.fcmToken(newToken)
				.isActive(true)
				.build();
		userDeviceRepository.save(updatedDevice);
	}

	/**
	 * 디바이스 비활성화
	 */
	public void deactivateDevice(Member member, String deviceId) {
		Optional<UserDevice> device = userDeviceRepository.findByMemberAndDeviceId(member, deviceId);
		device.ifPresent(d -> {
			UserDevice deactivated = UserDevice.builder()
					.id(d.getId())
					.member(d.getMember())
					.deviceType(d.getDeviceType())
					.deviceId(d.getDeviceId())
					.fcmToken(d.getFcmToken())
					.isActive(false)
					.build();
			userDeviceRepository.save(deactivated);
		});
	}
}
