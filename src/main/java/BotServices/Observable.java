package BotServices;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers(Object object);
}
