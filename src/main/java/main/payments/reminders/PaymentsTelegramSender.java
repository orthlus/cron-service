package main.payments.reminders;

import art.aelaort.telegram.entity.RemindToSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class PaymentsTelegramSender extends TelegramSender {
	private final KeyboardsProvider keyboards;
	@Value("${telegram.admin.id}")
	private long adminId;
	protected PaymentsTelegramSender(
			@Value("${payments.telegram.bot.token}")
			String token,
			KeyboardsProvider keyboards) {
		super(token);
		this.keyboards = keyboards;
	}

	public void sendRemind(RemindToSend remind) {
		String msg = remind.getName();
		InlineKeyboardMarkup keyboard = keyboards.getRemindButtons(remind);
		send(msg, keyboard);
	}

	private void send(String text, ReplyKeyboard keyboard) {
		SendMessage message = SendMessage.builder()
				.chatId(adminId)
				.text(text)
				.replyMarkup(keyboard)
				.build();
		try {
			execute(message);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}
}
