package main.habr;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private final HabrTelegram habrTelegram;
	private final HabrStorage habrStorage;

	@Scheduled(cron = "${cron.habr.scan}", zone = "Europe/Moscow")
	@Retryable(backoff = @Backoff(delay = 60_000))
	public void scanNewPosts() {
		Set<String> posts = habrClient.getPostsFromRss();
		Set<String> news = habrClient.getNewsFromRss();
		Set<String> lastPosts = habrStorage.getLastRssPosts();
		Set<String> lastNews = habrStorage.getLastRssNews();

		Sets.SetView<String> newPosts = Sets.difference(posts, lastPosts);
		Sets.SetView<String> newNews = Sets.difference(news, lastNews);

		newPosts.stream()
				.filter(habrClient::isPostHasABBR)
				.forEach(url -> habrTelegram.sendChannelMessage(telegramMsg(url)));
		newNews.stream()
				.filter(habrClient::isPostHasABBR)
				.forEach(url -> habrTelegram.sendChannelMessage(telegramMsg(url)));

		habrStorage.saveLastRssPosts(posts);
		habrStorage.saveLastRssNews(news);
		log.info("habr finish scan, {} new posts, {} new news", newPosts.size(), newNews.size());
	}

	private String telegramMsg(String url) {
		return url;
	}
}
