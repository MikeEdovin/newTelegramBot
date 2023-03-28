package com.newbot.launch;

import Config.GeoWeatherConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GeoWeatherConfig.class})
public class NewApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewApplication.class, args);
	}

}
