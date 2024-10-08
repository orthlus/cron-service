package main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplatesConfig {
	@Bean
	public RestTemplate habr(RestTemplateBuilder restTemplateBuilder,
							 @Value("${habr.http.timeout}") int timeout,
							 @Value("${habr.http.delay}") int delay) {
		String baseUrl = "https://habr.com/ru";
		return restTemplateBuilder.rootUri(baseUrl)
				.setConnectTimeout(Duration.ofSeconds(timeout))
				.setReadTimeout(Duration.ofSeconds(timeout))
				.interceptors((request, body, execution) -> {
					try {
						TimeUnit.SECONDS.sleep(delay);
					} catch (InterruptedException ignored) {
					}
					return execution.execute(request, body);
				})
				.build();
	}

	@Bean
	public RestTemplate cloudflare(RestTemplateBuilder restTemplateBuilder,
							 @Value("${cloudflare.dns.token}") String token,
							 @Value("${cloudflare.dns.url}") String url) {
		return restTemplateBuilder
				.rootUri(url)
				.defaultHeader("authorization", "Bearer " + token)
				.defaultHeader("content-type", "application/json")
				.build();
	}

	@Bean
	public RestTemplate telegramBackuper(RestTemplateBuilder restTemplateBuilder,
							 @Value("${telegram.backuper.tdl.url}") String url) {
		return restTemplateBuilder
				.rootUri(url)
				.build();
	}
}
