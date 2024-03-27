package main.habr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class HabrTelegram extends TelegramSender {
	@Value("${habr.telegram.channel_id}")
	private long channelId;

	public HabrTelegram(@Value("${habr.telegram.bot.token}") String token) {
		super(token);
	}

	public void sendChannelMessage(String text) {
		SendMessage message = SendMessage.builder()
				.chatId(channelId)
				.text(text)
				.disableWebPagePreview(true)
				.build();
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error("habr send error {}", text, e);
			throw new RuntimeException(e);
		}
	}
}
