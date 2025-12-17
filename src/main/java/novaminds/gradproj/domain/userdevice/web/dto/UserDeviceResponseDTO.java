package novaminds.gradproj.domain.userdevice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.userdevice.entity.DeviceType;

import java.time.LocalDateTime;

public class UserDeviceResponseDTO {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class DeviceInfo {
		private Long id;
		private DeviceType deviceType;
		private String deviceId;
		private boolean isActive;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
}
