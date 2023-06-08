package BotServices;

public interface MessageSender extends Runnable{
    void run();
    void stop();
}
