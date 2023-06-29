package com.newbot.launch;

import BotPackage.Bot;
import BotServices.MessageReceiver;
import BotServices.MessageSender;
import BotServices.Notifier;
import Config.*;
import Service.UserService;
import States.*;
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
		Notifier notifier=context.getBean(Notifier.class);
		MessageReceiver receiver=context.getBean(MessageReceiver.class);
		bot.add(receiver);

		try {
			bot.botConnect();
		}catch (TelegramApiException e){
			e.printStackTrace();
		}
/*
		Thread notifierThread=new Thread(notifier);
		notifierThread.setDaemon(true);
		notifierThread.setName("NotifierThread");
		notifierThread.start();

 */






	}


}
