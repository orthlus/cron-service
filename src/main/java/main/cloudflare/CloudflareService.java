package main.cloudflare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.cloudflare.dto.ZoneRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudflareService {
	private final CloudflareClient client;
	private final ObjectMapper jacksonObjectMapper;
	private final CloudflareBackuper cloudflareBackuper;

	@Scheduled(cron = "${cron.dns.backup}")
	public void backupDns() {
		String data = serializeData(getDataFromApi());
		String filename = now() + ".json";
		cloudflareBackuper.uploadFileContent(filename, data);
		log.info("cloudflare backup {} complete", filename);
	}

	public String serializeData(List<ZoneRecord> records) {
		try {
			return jacksonObjectMapper.writeValueAsString(records);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ZoneRecord> getDataFromApi() {
		List<ZoneRecord> result = new ArrayList<>();

		List<String> zoneIds = client.zonesIds();
		for (String zoneId : zoneIds) {
			List<ZoneRecord> records = client.getRecordsByZoneId(zoneId);
			result.addAll(records);
		}

		return result;
	}

	private static String now() {
		return LocalDateTime.now().toString()
				.replaceAll(":", "_")
				.replaceAll("-", "_")
				.replace(".", "_");
	}
}
