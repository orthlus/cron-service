package main;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScheduledTaskManager extends ScheduledTaskRegistrar {

	@PostConstruct
	public void printTasks() {
		String joined = String.join("\n",
				getScheduledTriggerTasksInfo(),
				getScheduledCronTasksInfo(),
				getScheduledFixedDelayTasksInfo(),
				getScheduledFixedRateTasksInfo()
		);
		log.info("scheduled jobs: {}", joined);
	}

	private String getScheduledTriggerTasksInfo() {
		return getTriggerTaskList().stream()
				.map(task -> {
					Trigger trigger = task.getTrigger();
					if (trigger instanceof CronTrigger t) {
						return t.getExpression();
					} else if (trigger instanceof PeriodicTrigger t) {
						return intervalTaskDataToString(
								Objects.requireNonNull(t.getInitialDelayDuration()).toMillis(),
								t.getPeriodDuration().toMillis());
					} else {
						return "";
					}
				})
				.collect(Collectors.joining("\n"));
	}

	private String intervalTaskDataToString(long initialDelayDuration, long intervalDuration) {
		return "init delay: %dms, interval: %dms".formatted(initialDelayDuration, intervalDuration);
	}

	private String intervalTaskToString(IntervalTask task) {
		return intervalTaskDataToString(
				task.getInitialDelayDuration().toMillis(),
				task.getIntervalDuration().toMillis());
	}

	private String getScheduledFixedRateTasksInfo() {
		return getFixedRateTaskList().stream()
				.map(this::intervalTaskToString)
				.collect(Collectors.joining("\n"));
	}

	private String getScheduledFixedDelayTasksInfo() {
		return getFixedDelayTaskList().stream()
				.map(this::intervalTaskToString)
				.collect(Collectors.joining("\n"));
	}

	private String getScheduledCronTasksInfo() {
		return getCronTaskList().stream()
				.map(CronTask::getExpression)
				.collect(Collectors.joining("\n"));
	}
}
