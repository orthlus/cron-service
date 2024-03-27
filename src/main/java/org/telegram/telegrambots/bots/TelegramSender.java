package org.telegram.telegrambots.bots;

public class TelegramSender extends DefaultAbsSender {
	protected TelegramSender(String botToken) {
		super(new DefaultBotOptions(), botToken);
	}
}
