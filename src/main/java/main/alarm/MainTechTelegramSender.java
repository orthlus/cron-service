package main.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainTechTelegramSender {
	private Optional<MessageInfo> savedMessage;
	private final MainTechTelegramClient mainTechTelegramClient;

	public void sendAlarm(String link) {
		MessageInfo message = mainTechTelegramClient.sendWithOutPreview("Го!\n" + link);
		savedMessage = Optional.of(message);
	}

	public void deleteLastAlarmMessage() {
		if (savedMessage.isPresent()) {
			MessageInfo message = savedMessage.get();
			mainTechTelegramClient.deleteMessage(message);
			savedMessage = Optional.empty();
		}
	}
}
