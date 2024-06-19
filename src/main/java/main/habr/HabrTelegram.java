package main.habr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static art.aelaort.TelegramClientHelpers.execute;

@Slf4j
@Component
@RequiredArgsConstructor
public class HabrTelegram {
	@Value("${habr.telegram.channel_id}")
	private long channelId;
	@Qualifier("habrTelegramClient")
	private final TelegramClient telegramClient;


	public void sendChannelMessage(String text) {
		SendMessage message = SendMessage.builder()
				.chatId(channelId)
				.text(text)
				.disableWebPagePreview(true)
				.build();
		execute(message, telegramClient);
	}
}
