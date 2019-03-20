package org.lpro.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class BackofficeApplication {

	private final int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB
	public static void main(String[] args) {
		SpringApplication.run(BackofficeApplication.class, args);


	}

}

