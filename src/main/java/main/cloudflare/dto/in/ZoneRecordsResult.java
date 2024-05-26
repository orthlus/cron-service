package main.cloudflare.dto.in;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import main.cloudflare.dto.ZoneRecord;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneRecordsResult {
	@JsonProperty("result")
	private List<ZoneRecord> result;
	@JsonProperty
	private boolean success;
}
