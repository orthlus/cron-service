package main;

import java.time.LocalDateTime;

public class Utils {
	public static String now() {
		return LocalDateTime.now().toString()
				.replaceAll(":", "-")
				.replace(".", "-");
	}
}
