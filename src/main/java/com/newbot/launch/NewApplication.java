package com.newbot.launch;

import BotPackage.Bot;
import BotServices.Worker;
import Config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@Import({GeoWeatherConfig.class, DBConfig.class, BotConfig.class, ServiceConfig.class, StatesConfig.class, AsyncConfig.class})
public class NewApplication {
	public static void main(String[] args) {

		ApplicationContext context=
		SpringApplication.run(NewApplication.class, args);
		Bot bot=context.getBean(Bot.class);
		Worker worker=context.getBean(Worker.class);
		bot.add(worker);

		try {
			bot.botConnect();
		}catch (TelegramApiException e){
			e.printStackTrace();
		}







	}


}
