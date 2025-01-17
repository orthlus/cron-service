package main.rest;


import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.stream.Stream;

import static java.nio.file.Files.*;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.time.LocalDateTime.now;
import static java.time.temporal.WeekFields.of;
import static java.util.Locale.ENGLISH;
import static java.util.UUID.randomUUID;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.io.FileUtils.deleteDirectory;

@Slf4j
@Component
public class GitJob {
	@Value("${app.git.login}")
	private String login;
	@Value("${app.git.password}")
	private String pass;
	@Value("${app.git.url}")
	private String gitUrl;

	@Scheduled(cron = "${cron.git.execute}")
	@Retryable(backoff = @Backoff(delay = 60000))
	public void execute() throws GitAPIException, IOException, InterruptedException {
		CredentialsProvider cred = creds();

		Path dir = createDirectory(Path.of("/tmp").resolve(randomUUID().toString()));

		var cloneCommand = Git.cloneRepository()
				.setURI(gitUrl)
				.setDirectory(dir.toFile())
				.setCredentialsProvider(cred);

		int bound = now().get(of(ENGLISH).weekOfYear()) % 2 == 0 ? 5 : 30;
		int count = new SecureRandom().nextInt(bound) + 1;

		try (Git git = cloneCommand.call()) {
			Path gitDir = git.getRepository().getWorkTree().toPath();
			try (Stream<Path> textFile = walk(gitDir).filter(this::filter)) {
				Path file = textFile.findFirst().orElseThrow();
				for (int i = 0; i < count; i++) {
					SECONDS.sleep(1);
					doChanges(file, git);
				}
			}

			git.push().setCredentialsProvider(cred).call();
		}

		deleteDirectory(dir.toFile());
	}

	private void doChanges(Path file, Git git) throws IOException, GitAPIException {
		smartChangeFile(file);

		String message = "add again at " + now();
		git.commit()
				.setMessage(message)
				.setAuthor("", login)
				.setCommitter("", login)
				.setAll(true)
				.call();
	}

	private void smartChangeFile(Path file) throws IOException {
		long sizeInBytes = size(file);
		var option = sizeInBytes > 10000 ? TRUNCATE_EXISTING : APPEND;

		writeString(file, "\ntext", option);
	}

	private boolean filter(Path f) {
		return f.getFileName().toString().equals("text.txt");
	}

	private CredentialsProvider creds() {
		return new UsernamePasswordCredentialsProvider(login, pass);
	}
}
