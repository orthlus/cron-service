package main.habr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.habr.rss.RssFeed;
import main.habr.rss.RssMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;

import static org.mapstruct.factory.Mappers.getMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class HabrClient {
	private final RssMapper rssMapper = getMapper(RssMapper.class);
	@Qualifier("habr")
	private final RestTemplate client;

	public Set<String> getNewsFromRss() {
		RssFeed object = client.getForObject("/rss/news/?limit=100", RssFeed.class);
		return rssMapper.map(object.getPosts());
	}

	public Set<String> getPostsFromRss() {
		RssFeed object = client.getForObject("/rss/all/?limit=100", RssFeed.class);
		return rssMapper.map(object.getPosts());
	}

	public boolean isPostHasABBR(String url) {
		try {
			String pageContent = client.getForObject(URI.create(url), String.class);
			return pageContent.contains("class=\"habraabbr\"");
		} catch (Exception e) {
			return false;
		}
	}
}
