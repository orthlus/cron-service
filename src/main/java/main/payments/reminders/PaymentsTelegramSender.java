package main.payments.reminders;

import art.aelaort.telegram.entity.RemindToSend;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static art.aelaort.TelegramClientHelpers.execute;

@Component
@RequiredArgsConstructor
public class PaymentsTelegramSender {
	private final KeyboardsProvider keyboards;
	@Qualifier("paymentsTelegramClient")
	private final TelegramClient telegramClient;
	@Value("${telegram.admin.id}")
	private long adminId;

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
		execute(message, telegramClient);
	}
}
