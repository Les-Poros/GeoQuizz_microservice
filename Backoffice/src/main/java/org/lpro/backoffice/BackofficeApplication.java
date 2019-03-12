package org.lpro.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackofficeApplication {

	private final int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB
	public static void main(String[] args) {
		SpringApplication.run(BackofficeApplication.class, args);
	}

}

