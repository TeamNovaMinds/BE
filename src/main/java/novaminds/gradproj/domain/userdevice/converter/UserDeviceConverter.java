package novaminds.gradproj.domain.userdevice.converter;

import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.userdevice.entity.DeviceType;
import novaminds.gradproj.domain.userdevice.entity.UserDevice;
import novaminds.gradproj.domain.userdevice.web.dto.UserDeviceRequestDTO;
import novaminds.gradproj.domain.userdevice.web.dto.UserDeviceResponseDTO;

public class UserDeviceConverter {

	public static UserDevice toUserDevice(Member member, UserDeviceRequestDTO.RegisterDeviceRequest request) {
		return UserDevice.builder()
				.member(member)
				.deviceType(request.getDeviceType())
				.deviceId(request.getDeviceId())
				.fcmToken(request.getExpoPushToken())
				.isActive(true)
				.build();
	}

	public static UserDeviceResponseDTO.DeviceInfo toDeviceInfo(UserDevice userDevice) {
		return UserDeviceResponseDTO.DeviceInfo.builder()
				.id(userDevice.getId())
				.deviceType(userDevice.getDeviceType())
				.deviceId(userDevice.getDeviceId())
				.isActive(userDevice.isActive())
				.createdAt(userDevice.getCreatedAt())
				.updatedAt(userDevice.getUpdatedAt())
				.build();
	}
}
