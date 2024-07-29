package main.habr;

import art.aelaort.S3Params;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Set;

import static art.aelaort.S3ClientProvider.client;

@Slf4j
@Component
@RequiredArgsConstructor
public class HabrStorage {
	@Qualifier("habr")
	private final S3Params habr;
	@Value("${habr.s3.bucket}")
	private String bucket;
	private final String postsFile = "last-posts.csv";
	private final String newsFile = "last-news.csv";

	public Set<String> getLastRssPosts() {
		try (S3Client client = client(habr)) {
			String[] split = client.getObjectAsBytes(builder -> builder
							.key(postsFile)
							.bucket(bucket))
					.asUtf8String()
					.split("\n");
			return Set.of(split);
		}
	}

	public Set<String> getLastRssNews() {
		try (S3Client client = client(habr)) {
			String[] split = client.getObjectAsBytes(builder -> builder
							.key(newsFile)
							.bucket(bucket))
					.asUtf8String()
					.split("\n");
			return Set.of(split);
		}
	}

	public void saveLastRssPosts(Set<String> posts) {
		try (S3Client client = client(habr)) {
			String csvString = String.join("\n", posts);
			client.putObject(builder -> builder
							.key(postsFile)
							.bucket(bucket),
					RequestBody.fromString(csvString));
		}
	}

	public void saveLastRssNews(Set<String> news) {
		try (S3Client client = client(habr)) {
			String csvString = String.join("\n", news);
			client.putObject(builder -> builder
							.key(newsFile)
							.bucket(bucket),
					RequestBody.fromString(csvString));
		}
	}
}
