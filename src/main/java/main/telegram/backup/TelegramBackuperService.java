package main.telegram.backup;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TelegramBackuperService {
	private final TelegramBackuperClient telegramBackuperClient;
	private final TelegramBackuperS3 telegramBackuperS3;
	@Value("${telegram.backuper.filename}")
	private String s3Filename;

	@Scheduled(cron = "${cron.telegram.backup}", zone = "Europe/Moscow")
	public void backup() {
		String chatBackupContent = telegramBackuperClient.getChatBackupContent();
		String time = LocalDateTime.now().toString()
				.replaceAll(":", "-")
				.replace(".", "-")
				.replace("T", "-");
		String fileName = "%s-%s.json".formatted(s3Filename, time);

		telegramBackuperS3.uploadFileContent(fileName, chatBackupContent);
	}
}
