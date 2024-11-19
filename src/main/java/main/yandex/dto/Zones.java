package main.yandex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Zones {
	@JsonProperty
	private List<Zone> dnsZones;

	public List<ZoneInfo> zoneInfoList() {
		return dnsZones.stream()
				.map(zone -> new ZoneInfo(zone.id, zone.zone))
				.toList();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Zone {
		@JsonProperty
		private String id;
		@JsonProperty
		private String zone;
	}
}
