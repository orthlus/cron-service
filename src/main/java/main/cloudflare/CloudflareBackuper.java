package main.cloudflare;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import main.rest.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

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
			AmazonS3 s3 = client();
			TransferManager tm = TransferManagerBuilder.standard()
					.withS3Client(s3)
					.build();
			byte[] bytes = fileContent.getBytes();
			ByteArrayInputStream fileData = new ByteArrayInputStream(bytes);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(bytes.length);
			Upload upload = tm.upload(bucket, fileId, fileData, objectMetadata);
			upload.waitForCompletion();
			tm.shutdownNow();
			s3.shutdown();
		} catch (InterruptedException e) {
			log.error("s3 error - upload file {}/{}", bucket, fileId, e);
			throw new RuntimeException(e);
		}
	}

	private AmazonS3 client() {
		return client(id, key, url, region);
	}
}
