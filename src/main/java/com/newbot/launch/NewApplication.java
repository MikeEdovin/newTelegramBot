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
@Import({GeoWeatherConfig.class, DBConfig.class, BotConfig.class, ServiceConfig.class, StatesConfig.class})
public class NewApplication {
	private static final int PRIORITY_FOR_SENDER = 1;
	private static final int PRIORITY_FOR_RECEIVER = 3;
	public static void main(String[] args) {

		ApplicationContext context=
		SpringApplication.run(NewApplication.class, args);
		Bot bot=context.getBean(Bot.class);
		MessageReceiver messageReceiver=context.getBean(MessageReceiver.class);
		MessageSender messageSender=context.getBean(MessageSender.class);
		Notifier notifier=context.getBean(Notifier.class);
		State mainState=context.getBean("mainState", MainState.class);
		State setCityState=context.getBean("setCityState", SetCityState.class);
		State newInputState=context.getBean("newInputState", NewInputState.class);
		State notificationsState=context.getBean("notificationsState", NotificationsState.class);

		mainState.addObserver(messageSender);
		setCityState.addObserver(messageSender);
		newInputState.addObserver(messageSender);
		notificationsState.addObserver(messageSender);
		notificationsState.addObserver(notifier);


		bot.addObserver(messageReceiver);



		try {
			bot.botConnect();
		}catch (TelegramApiException e){
			e.printStackTrace();
		}

		Thread notifierThread=new Thread(notifier);
		notifierThread.setDaemon(true);
		notifierThread.setName("NotifierThread");
		notifierThread.start();

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
