package main.cloudflare;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import main.rest.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudflareBackuper extends S3Client {
	@Value("${cloudflare.backuper.id}")
	private String id;
	@Value("${cloudflare.backuper.key}")
	private String key;
	@Value("${cloudflare.backuper.url}")
	private String url;
	@Value("${cloudflare.backuper.region}")
	private String region;
	@Value("${cloudflare.backuper.bucket}")
	private String bucket;

	public void uploadFileContent(String fileId, String fileContent) {
		try {
			uploadFileContent(fileId, fileContent, client(), bucket);
		} catch (InterruptedException e) {
			log.error("s3 error - upload file {}/{}", bucket, fileId, e);
			throw new RuntimeException(e);
		}
	}

	private AmazonS3 client() {
		return client(id, key, url, region);
	}
}
