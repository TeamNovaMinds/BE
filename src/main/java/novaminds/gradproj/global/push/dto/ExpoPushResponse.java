package novaminds.gradproj.global.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpoPushResponse {

	@JsonProperty("data")
	private Data data;

	@Getter
	@NoArgsConstructor
	public static class Data {
		@JsonProperty("status")
		private String status;

		@JsonProperty("id")
		private String id;

		@JsonProperty("message")
		private String message;
	}
}
