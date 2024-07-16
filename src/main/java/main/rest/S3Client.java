package main.rest;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;

public abstract class S3Client {
	public void uploadFileContent(String fileId, String fileContent, AmazonS3 s3, String bucket)
			throws InterruptedException {
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
	}

	public AmazonS3 client(String id, String key, String url, String region) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(id, key);
		AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
		var configuration = new AmazonS3ClientBuilder.EndpointConfiguration(url, region);
		return AmazonS3ClientBuilder.standard()
				.withCredentials(provider)
				.withEndpointConfiguration(configuration)
				.build();
	}
}
