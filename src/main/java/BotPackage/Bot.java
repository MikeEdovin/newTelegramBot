package BotPackage;

import BotServices.Observable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public abstract class Bot extends TelegramLongPollingBot implements Observable {


    abstract void setStartedStatus(boolean status);
    abstract public String getBotToken();
    abstract public void botConnect() throws TelegramApiException;
}
