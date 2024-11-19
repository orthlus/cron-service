package main.yandex;

import art.aelaort.YandexIAMSupplier;
import lombok.RequiredArgsConstructor;
import main.yandex.dto.ZoneInfo;
import main.yandex.dto.Zones;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YandexDnsService {
	private final YandexIAMSupplier yandexIAMSupplier;
	private final RestTemplate yandexDns;
	@Value("${yandex.dns.folder_id}")
	private String folderId;

	public String getZoneRecordsStringByZoneId(String zoneId) {
		return yandexDns.exchange(
						"/dns/v1/zones/%s:listRecordSets".formatted(zoneId),
						HttpMethod.GET,
						entityBearerToken(yandexIAMSupplier.getToken()),
						String.class)
				.getBody();
	}

	public List<ZoneInfo> getZones() {
		return yandexDns.exchange(
						"/dns/v1/zones?folderId=%s".formatted(folderId),
						HttpMethod.GET,
						entityBearerToken(yandexIAMSupplier.getToken()),
						Zones.class)
				.getBody().zoneInfoList();
	}

	private HttpEntity<?> entityBearerToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		return new HttpEntity<>(headers);
	}
}
