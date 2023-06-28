package BotServices;

import Entities.User;

import java.util.ArrayList;
import java.util.List;

public interface Notifier extends Runnable,Observer{
    void gotNotifListUpdate(User user);
    void run();
    void stop();
}
