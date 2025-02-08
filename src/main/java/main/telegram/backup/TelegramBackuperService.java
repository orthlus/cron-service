package main.telegram.backup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBackuperService {
	@Qualifier("telegramBackuper")
	private final RestTemplate restTemplate;

	@Scheduled(cron = "${cron.telegram.backup}", zone = "Europe/Moscow")
	public void backup() {
		restTemplate.getForObject("/backup", Void.class);
		log.info("telegram backup complete");
	}
}
