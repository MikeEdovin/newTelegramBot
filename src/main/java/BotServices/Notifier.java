package BotServices;

import Entities.User;

import java.util.ArrayList;
import java.util.List;

public interface Notifier extends Runnable,Observer{
    List<User> usersWithNotifications=new ArrayList<>();
    void run();
    void stop();
}
