package BotServices;

public interface IMessageSender extends Runnable{
    void run();
    void stop();
}
