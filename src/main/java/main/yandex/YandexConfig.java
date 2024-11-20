package main.yandex;

import art.aelaort.YandexIAMConfig;
import art.aelaort.YandexIAMSupplier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(YandexIAMConfig.class)
@Configuration
public class YandexConfig {
	@Bean
	public YandexIAMSupplier yaIAMSupplier(YandexIAMConfig yandexIAMConfig, RestTemplate yandexDns) {
		return new YandexIAMSupplier(yandexDns, yandexIAMConfig);
	}
}
