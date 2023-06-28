package BotServices;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Observable {
    void add(Observer observer);
    void notifyObservers(Update update);
}
