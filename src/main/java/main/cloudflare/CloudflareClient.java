package main.cloudflare;

import lombok.RequiredArgsConstructor;
import main.cloudflare.dto.ZoneRecord;
import main.cloudflare.dto.in.ZoneRecordsResult;
import main.cloudflare.dto.in.ZonesResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CloudflareClient {
	@Qualifier("cloudflare")
	private final RestTemplate restTemplate;

	public List<String> zonesIds() {
		ZonesResult object = restTemplate.getForObject("/zones", ZonesResult.class);
		return object.isSuccess() ? object.getIds() : List.of();
	}

	public List<ZoneRecord> getRecordsByZoneId(String zoneId) {
		String url = "/zones/" + zoneId + "/dns_records";
		ZoneRecordsResult object = restTemplate.getForObject(url, ZoneRecordsResult.class);
		return object.isSuccess() ? object.getResult() : List.of();
	}
}
