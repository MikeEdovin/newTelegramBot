package BotServices;

import BotPackage.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSenderImpl implements MessageSender {
    @Autowired
    Bot bot;
    @Override
    @Async
    public void sendMessageAsync(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {

        }
    }
}

