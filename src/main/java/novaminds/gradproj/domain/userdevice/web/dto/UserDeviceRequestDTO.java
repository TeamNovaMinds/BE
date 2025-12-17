package novaminds.gradproj.domain.userdevice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.userdevice.entity.DeviceType;

public class UserDeviceRequestDTO {

	@Getter
	@NoArgsConstructor
	public static class RegisterDeviceRequest {

		@NotNull(message = "디바이스 타입은 필수입니다")
		private DeviceType deviceType;

		@NotBlank(message = "디바이스 ID는 필수입니다")
		private String deviceId;

		@NotBlank(message = "Expo Push Token은 필수입니다")
		private String expoPushToken;
	}

	@Getter
	@NoArgsConstructor
	public static class UpdateTokenRequest {

		@NotBlank(message = "Expo Push Token은 필수입니다")
		private String expoPushToken;
	}
}
