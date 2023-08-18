package com.newbot.launch;

import BotPackage.Bot;
import BotServices.Worker;
import Config.*;
import GeoWeatherPackage.ReactiveGeoWeatherProviderImpl;
import Repository.ReactiveUserRepository;
import Service.ReactiveUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication(scanBasePackageClasses = {ReactiveUserServiceImpl.class, ReactiveUserRepository.class, ReactiveGeoWeatherProviderImpl.class})
@EnableWebFlux
@EnableR2dbcRepositories
@Import({GeoWeatherConfig.class, DBConfig.class, BotConfig.class, ServiceConfig.class, StatesConfig.class, AsyncConfig.class})
public class NewApplication {
	final static Logger logger= LoggerFactory.getLogger(NewApplication.class);
	public static void main(String[] args) {

		ApplicationContext context=
		SpringApplication.run(NewApplication.class, args);
		Bot bot=context.getBean(Bot.class);
		Worker worker=context.getBean(Worker.class);
		bot.add(worker);
		try {
			bot.botConnect();
		}catch (TelegramApiException e){
			logger.warn(e.getMessage());
		}








	}


}
