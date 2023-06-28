package BotServices;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender{
    void sendMessageAsync(SendMessage message);

}
