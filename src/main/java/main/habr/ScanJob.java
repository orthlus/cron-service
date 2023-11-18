package main.habr;

import com.google.common.collect.Sets;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScanJob {
	private final HabrClient habrClient;
	private final HabrRepo repo;
	@Value("${local.telegram_sender.url}")
	private String telegramUrl;
	private Telegram telegram;

	@PostConstruct
	private void init() {
		telegram = Feign.builder().target(Telegram.class, telegramUrl);
	}

	@Scheduled(cron = "0 0 0/3 * * *", zone = "Europe/Moscow")
	@Retryable(backoff = @Backoff(delay = 60_000))
	public void scanNewPosts() {
		Set<String> posts = habrClient.getPostsFromRss();
		Set<String> news = habrClient.getNewsFromRss();
		Set<String> lastPosts = repo.getLastRssPosts();
		Set<String> lastNews = repo.getLastRssNews();

		Sets.SetView<String> newPosts = Sets.difference(posts, lastPosts);
		Sets.SetView<String> newNews = Sets.difference(news, lastNews);

		newPosts.stream()
				.filter(habrClient::isPostHasABBR)
				.forEach(url -> telegram.send(telegramMsg(url)));
		newNews.stream()
				.filter(habrClient::isPostHasABBR)
				.forEach(url -> telegram.send(telegramMsg(url)));

		repo.saveLastRssPosts(posts);
		repo.saveLastRssNews(news);
	}

	interface Telegram {
		@RequestLine("POST /send/habr?text={text}")
		void send(@Param("text") String text);
	}

	private String telegramMsg(String url) {
		return "Новый пост с аббревиатурой:\n" + url;
	}
}
