package main.cloudflare;

import art.aelaort.S3Params;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import static art.aelaort.S3ClientProvider.client;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudflareBackuper {
	@Qualifier("cloudflare")
	private final S3Params s3Config;
	@Value("${cloudflare.backuper.s3.bucket}")
	private String bucket;

	public void uploadFileContent(String fileId, String fileContent) {
		try (S3Client client = client(s3Config)) {
			client.putObject(builder -> builder
							.key(fileId)
							.bucket(bucket)
							.build(),
						RequestBody.fromString(fileContent));
		}
	}
}
