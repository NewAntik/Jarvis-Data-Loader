package ua.jarvis.data.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ua.jarvis.data.loader.service.UserDataImporter;

@SpringBootApplication
public class JarvisDataLoaderApplication {
	public static void main(final String[] args) {
		SpringApplication.run(JarvisDataLoaderApplication.class, args);
	}

	@Bean
	CommandLineRunner run(final UserDataImporter userDataImporter) {
		return args -> userDataImporter.readAndSaveData();
	}
}
