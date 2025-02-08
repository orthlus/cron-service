package main;

import art.aelaort.DefaultS3Params;
import art.aelaort.S3Params;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
	@Value("${main.s3.url}")
	private String url;
	@Value("${main.s3.region}")
	private String region;

	@Bean
	public S3Params yandexDnsS3(
			@Value("${yandex.dns.s3.id}") String id,
			@Value("${yandex.dns.s3.key}") String key
	) {
		return new DefaultS3Params(id, key, url, region);
	}

	@Bean
	public S3Params habrS3(
			@Value("${habr.s3.id}") String id,
			@Value("${habr.s3.key}") String key
	) {
		return new DefaultS3Params(id, key, url, region);
	}
}
