package BotServices;

public interface MessageSender extends Runnable,Observer{
    void run();
    void stop();
}
