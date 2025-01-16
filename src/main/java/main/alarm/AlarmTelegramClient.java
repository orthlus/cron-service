package main.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static art.aelaort.TelegramClientHelpers.execute;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmTelegramClient {
	@Qualifier("alarmTelegramClient")
	private final TelegramClient telegramClient;
	@Value("${telegram.admin.id}")
	private long adminId;

	public void sendAlarm(String link) {
		sendWithOutPreview("Го!\n" + link);
	}

	private MessageInfo sendWithOutPreview(String text) {
		SendMessage message = SendMessage.builder()
				.chatId(adminId)
				.text(text)
				.disableWebPagePreview(true)
				.build();
		Message execute = execute(message, telegramClient);
		return new MessageInfo(execute.getChatId(), execute.getMessageId());
	}
}
