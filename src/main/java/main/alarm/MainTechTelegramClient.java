package main.alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class MainTechTelegramClient extends TelegramSender {
	@Value("${telegram.admin.id}")
	private long adminId;

	protected MainTechTelegramClient(@Value("${main_tech.telegram.bot.token}") String token) {
		super(token);
	}

	public void deleteMessage(MessageInfo message) {
		long chatId = message.chatId();
		int messageId = message.messageId();
		try {
			execute(new DeleteMessage(String.valueOf(chatId), messageId));
		} catch (TelegramApiException e) {
			log.error("{} - Error delete message, chat {} messageId {}", this.getClass().getName(), chatId, messageId, e);
			throw new RuntimeException(e);
		}
	}

	public MessageInfo sendWithOutPreview(String text) {
		SendMessage message = SendMessage.builder()
				.chatId(adminId)
				.text(text)
				.disableWebPagePreview(true)
				.build();
		try {
			Message execute = execute(message);
			return new MessageInfo(execute.getChatId(), execute.getMessageId());
		} catch (TelegramApiException e) {
			log.error("main tech send error");
			throw new RuntimeException(e);
		}
	}
}
