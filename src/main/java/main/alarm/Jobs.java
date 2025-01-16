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
	@Value("${alarm.url}")
	private String url;
	@Value("${alarm2.url}")
	private String url2;
	private final AlarmTelegramClient telegram;

	@Scheduled(cron = "${cron.alarm.send}", zone = "Europe/Moscow")
	@Retryable(maxAttempts = 10, backoff = @Backoff(delay = 500))
	public void send() {
		telegram.sendAlarm(url);
	}

	@Scheduled(cron = "${cron.alarm2.send}", zone = "Europe/Moscow")
	@Retryable(maxAttempts = 10, backoff = @Backoff(delay = 500))
	public void send2() {
		telegram.sendAlarm(url2);
	}
}
