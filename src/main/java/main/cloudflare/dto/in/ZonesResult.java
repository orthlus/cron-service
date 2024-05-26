package main.cloudflare.dto.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZonesResult {
	@JsonProperty
	private List<Zone> result;
	@Getter
	@JsonProperty
	private boolean success;

	public List<String> getIds() {
		return result.stream().map(Zone::getId).toList();
	}
}
