package com.newbot.launch;

import BotPackage.Bot;
import BotServices.IMessageReceiver;
import BotServices.IMessageSender;
import Config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
@Import({GeoWeatherConfig.class, DBConfig.class, BotConfig.class, ServiceConfig.class, HandlersConfig.class, StatesConfig.class})
public class NewApplication {
	private static final int PRIORITY_FOR_SENDER = 1;
	private static final int PRIORITY_FOR_RECEIVER = 3;
	public static void main(String[] args) {

		ApplicationContext context=
		SpringApplication.run(NewApplication.class, args);
		Bot bot=context.getBean(Bot.class);
		IMessageReceiver messageReceiver=context.getBean(IMessageReceiver.class);
		IMessageSender messageSender=context.getBean(IMessageSender.class);

		try {
			bot.botConnect();
		}catch (TelegramApiException e){
			e.printStackTrace();
		}
		Thread receiver = new Thread(messageReceiver);
		receiver.setDaemon(true);
		receiver.setName("MsgReceiver");
		receiver.setPriority(PRIORITY_FOR_RECEIVER);
		receiver.start();

		Thread sender = new Thread(messageSender);
		sender.setDaemon(true);
		sender.setName("MsgSender");
		sender.setPriority(PRIORITY_FOR_SENDER);
		sender.start();



	}

}
