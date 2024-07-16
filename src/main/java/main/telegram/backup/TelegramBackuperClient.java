package main.telegram.backup;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TelegramBackuperClient {
	@Qualifier("telegramBackuper")
	private final RestTemplate restTemplate;

	public String getChatBackupContent() {
		return restTemplate.getForObject("/", String.class);
	}
}
