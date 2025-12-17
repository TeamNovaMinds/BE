package novaminds.gradproj.global.push.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpoPushMessage {

	@JsonProperty("to")
	private String to;

	@JsonProperty("title")
	private String title;

	@JsonProperty("body")
	private String body;

	@JsonProperty("data")
	private Object data;

	@JsonProperty("sound")
	@Builder.Default
	private String sound = "default";

	@JsonProperty("priority")
	@Builder.Default
	private String priority = "high";
}
