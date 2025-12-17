package novaminds.gradproj.domain.userdevice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.apiPayload.ApiResponse;
import novaminds.gradproj.domain.member.entity.Member;
import novaminds.gradproj.domain.member.service.security.auth.CurrentUser;
import novaminds.gradproj.domain.userdevice.converter.UserDeviceConverter;
import novaminds.gradproj.domain.userdevice.entity.UserDevice;
import novaminds.gradproj.domain.userdevice.service.command.UserDeviceCommandService;
import novaminds.gradproj.domain.userdevice.web.dto.UserDeviceRequestDTO;
import novaminds.gradproj.domain.userdevice.web.dto.UserDeviceResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "디바이스 관리", description = "사용자 디바이스 및 푸시 알림 토큰 관리 API")
public class UserDeviceController {

	private final UserDeviceCommandService userDeviceCommandService;

	@Operation(summary = "디바이스 등록/업데이트",
			description = "사용자 디바이스와 Expo Push Token을 등록하거나 업데이트합니다. 같은 디바이스 ID가 이미 존재하면 토큰만 업데이트됩니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "디바이스 등록 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다.")
	})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public ApiResponse<UserDeviceResponseDTO.DeviceInfo> registerDevice(
			@CurrentUser Member member,
			@Valid @RequestBody UserDeviceRequestDTO.RegisterDeviceRequest request
	) {
		UserDevice device = userDeviceCommandService.registerOrUpdateDevice(member, request);
		UserDeviceResponseDTO.DeviceInfo response = UserDeviceConverter.toDeviceInfo(device);
		return ApiResponse.onSuccess(response);
	}

	@Operation(summary = "디바이스 비활성화",
			description = "특정 디바이스를 비활성화합니다. 비활성화된 디바이스는 푸시 알림을 받지 않습니다.")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "디바이스 비활성화 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요한 서비스입니다."),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "디바이스를 찾을 수 없습니다.")
	})
	@DeleteMapping("/{deviceId}")
	public ApiResponse<String> deactivateDevice(
			@CurrentUser Member member,
			@PathVariable String deviceId
	) {
		userDeviceCommandService.deactivateDevice(member, deviceId);
		return ApiResponse.onSuccess("디바이스가 비활성화되었습니다.");
	}
}
