package main;

import art.aelaort.DefaultS3Params;
import art.aelaort.S3Params;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
	@Value("${cloudflare.backuper.s3.url}")
	private String url;
	@Value("${cloudflare.backuper.s3.region}")
	private String region;

	@Bean
	public S3Params cloudflare(
			@Value("${cloudflare.backuper.s3.id}") String id,
			@Value("${cloudflare.backuper.s3.key}") String key
	) {
		return new DefaultS3Params(id, key, url, region);
	}

	@Bean
	public S3Params telegram(
			@Value("${telegram.backuper.s3.id}") String id,
			@Value("${telegram.backuper.s3.key}") String key
	) {
		return new DefaultS3Params(id, key, url, region);
	}
}
