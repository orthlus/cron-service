package main.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Jobs {
	@Value("${main_tech.alarm.url}")
	private String url;
	private final MainTechTelegramSender telegram;

	@Scheduled(cron = "${cron.alarm.send}", zone = "Europe/Moscow")
	@Retryable(maxAttempts = 10, backoff = @Backoff(delay = 500))
	public void send() {
		telegram.sendAlarm(url);
	}

	@Scheduled(cron = "${cron.alarm.clean}", zone = "Europe/Moscow")
	@Retryable(maxAttempts = 10)
	public void clean() {
		telegram.deleteLastAlarmMessage();
	}
}
