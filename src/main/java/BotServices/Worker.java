package BotServices;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Worker extends Observer{
    void operate(Update update);

}
