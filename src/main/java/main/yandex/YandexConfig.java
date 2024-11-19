package main.yandex;

import art.aelaort.YandexIAMConfig;
import art.aelaort.YandexIAMSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Configuration
public class YandexConfig {
	@Bean
	public YandexIAMSupplier yaIAMSupplier(
			@Value("${yandex.iAMS3.s3url}") String s3url,
			@Value("${yandex.iAMS3.s3region}") String s3region,
			@Value("${yandex.iAMS3.s3id}") String s3id,
			@Value("${yandex.iAMS3.s3key}") String s3key,
			@Value("${yandex.iAMS3.s3bucket}") String s3bucket,
			@Value("${yandex.iAMS3.s3file}") String s3file,
			@Value("${yandex.iAMS3.funcurl}") URI funcurl,
			@Value("${yandex.iAMS3.funcsecret}") String funcsecret,
			RestTemplate yandexDns) {
		return new YandexIAMSupplier(yandexDns,
				new YandexIAMConfig(
						s3id,
						s3key,
						s3url,
						s3region,
						s3file,
						s3bucket,
						funcsecret,
						funcurl
				));
	}
}
