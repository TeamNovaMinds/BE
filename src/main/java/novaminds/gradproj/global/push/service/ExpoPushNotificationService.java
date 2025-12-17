package novaminds.gradproj.global.push.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import novaminds.gradproj.global.push.dto.ExpoPushMessage;
import novaminds.gradproj.global.push.dto.ExpoPushResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpoPushNotificationService {

	private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
	private final RestTemplate restTemplate;

	/**
	 * Expo Push Token을 사용하여 푸시 알림 발송
	 * @param expoPushToken Expo Push Token (ExponentPushToken[xxx] 형식)
	 * @param title 알림 제목
	 * @param body 알림 내용
	 * @param data 추가 데이터 (딥링크 등)
	 * @return 발송 성공 여부
	 */
	public boolean sendPushNotification(String expoPushToken, String title, String body, Map<String, Object> data) {
		try {
			ExpoPushMessage message = ExpoPushMessage.builder()
					.to(expoPushToken)
					.title(title)
					.body(body)
					.data(data)
					.build();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept", "application/json");
			headers.set("Accept-Encoding", "gzip, deflate");

			HttpEntity<ExpoPushMessage> request = new HttpEntity<>(message, headers);

			ExpoPushResponse response = restTemplate.postForObject(EXPO_PUSH_URL, request, ExpoPushResponse.class);

			if (response != null && response.getData() != null) {
				log.info("Expo push notification sent successfully. ID: {}", response.getData().getId());
				return true;
			} else {
				log.warn("Expo push notification response is null or invalid");
				return false;
			}
		} catch (Exception e) {
			log.error("Failed to send Expo push notification to token: {}", expoPushToken, e);
			return false;
		}
	}

	/**
	 * 오버로드: data 없이 발송
	 */
	public boolean sendPushNotification(String expoPushToken, String title, String body) {
		return sendPushNotification(expoPushToken, title, body, new HashMap<>());
	}
}
