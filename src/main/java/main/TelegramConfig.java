package main;

import art.aelaort.TelegramClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {
	@Bean
	public TelegramClient alarmTelegramClient(@Value("${alarm.telegram.bot.token}") String token) {
		return TelegramClientBuilder.builder()
				.token(token)
				.build();
	}
}
