package main.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Message {
	@JsonProperty
	private long chatId;
	@JsonProperty
	private int messageId;
}
