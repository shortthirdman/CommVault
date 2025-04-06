// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.stream.Collectors;

@EnableWebMvc
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = {"com.shortthirdman.commvault"})
public class CommVaultApplication {
	
	@Value("${server.port}")
	private String serverPort;

	@Value("${server.servlet.application-display-name}")
	String appName;

	public static void main(String[] args) {
		SpringApplication.run(CommVaultApplication.class, args);
	}

	@Bean
	public OpenAPI openApiSwagger() {
		Contact contact = new Contact()
				.email("shortthirdman@gmail.com")
				.name("ShortThirdMan")
				.url("https://www.shortthirdman.com");

		License mitLicense = new License()
				.name("MIT License")
				.url("https://choosealicense.com/licenses/mit/")
				.identifier("mit-license");

		var fullAppName = Arrays.stream(appName.split("-", -1))
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
		Info info = new Info()
				.title("CommVault")
				.version("1.0").contact(contact)
				.description("This API exposes endpoints to manage user accounts and credentials.")
				.termsOfService("https://www.shortthirdman.com/terms")
				.license(mitLicense)
				.summary(fullAppName);

		return new OpenAPI().info(info);
	}
}
