package main.rest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.extern.slf4j.Slf4j;
import main.Main;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BackupsCleanJob extends S3Client {
	@Value("${main_tech.backups.cleaner.id}")
	private String id;
	@Value("${main_tech.backups.cleaner.key}")
	private String key;
	@Value("${main_tech.storage.url}")
	private String url;
	@Value("${main_tech.storage.region}")
	private String region;
	@Value("${main_tech.backups.cleaner.days_keep}")
	private int daysKeep;
	@Value("${main_tech.backups.cleaner.count_keep}")
	private int countKeep;
	@Value("${main_tech.backups.cleaner.bucket}")
	private String bucket;
	@Value("${main_tech.backups.cleaner.groups}")
	private String[] groups;

//	@Scheduled(fixedRateString = "${cron.backups.execute.fixed_rate.days}", timeUnit = TimeUnit.DAYS)
	public void execute() {
		AmazonS3 s3 = client();

		ObjectListing objects = s3.listObjects(bucket);
		for (String group : groups) {
			Set<S3ObjectSummary> objectsInGroup = objects.getObjectSummaries()
					.stream()
					.filter(o -> o.getKey().startsWith(group))
					.collect(Collectors.toSet());

			if (objectsInGroup.size() > countKeep) {
				for (S3ObjectSummary object : objectsInGroup) {
					if (isObjectOlderNDays(object)) {
						log.info("deleting backup object {}", object.getKey());
						s3.deleteObject(bucket, object.getKey());
					}
				}
			}
		}

		s3.shutdown();
	}

	private boolean isObjectOlderNDays(S3ObjectSummary object) {
		return isOlder(convert(object.getLastModified()), LocalDateTime.now(Main.zone), daysKeep);
	}

	private boolean isOlder(LocalDateTime ldt1, LocalDateTime ldt2, int days) {
		return ldt1.isBefore(ldt2.minusDays(days));
	}

	private LocalDateTime convert(Date date) {
		return date.toInstant().atZone(Main.zone).toLocalDateTime();
	}

	private AmazonS3 client() {
		return client(id, key, url, region);
	}
}
