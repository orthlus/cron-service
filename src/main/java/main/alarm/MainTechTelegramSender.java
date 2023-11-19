package main.alarm;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class MainTechTelegramSender {
	@Value("${local.telegram_sender.url}")
	private String telegramUrl;
	private MainTechTelegram telegram;
	private Optional<Message> savedMessage;

	@PostConstruct
	private void init() {
		telegram = Feign.builder()
				.decoder(new JacksonDecoder())
				.encoder(new JacksonEncoder())
				.target(MainTechTelegram.class, telegramUrl);
	}

	interface MainTechTelegram {
		@RequestLine("POST /send/main-tech?text={text}")
		Message send(@Param("text") String text);

		@RequestLine("DELETE /delete/main-tech")
		void delete(Message message);
	}

	public void sendAlarm(String link) {
		Message message = telegram.send("Го!\n" + link);
		savedMessage = Optional.of(message);
	}

	public void deleteLastAlarmMessage() {
		if (savedMessage.isPresent()) {
			Message message = savedMessage.get();
			telegram.delete(message);
			savedMessage = Optional.empty();
		}
	}
}
