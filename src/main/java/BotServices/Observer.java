package BotServices;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Observer {
    void gotUpdateAsync(Update update);
}
