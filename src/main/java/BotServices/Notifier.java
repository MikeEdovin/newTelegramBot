package BotServices;

public interface Notifier extends Runnable{
    void run();
    void stop();
}
