package BotServices;

import Entities.CityData;
import Entities.Current;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NotifierImpl implements Notifier{
    @Autowired
    UserService userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;

    List<User> usersWithNotifications;
    //List<User> usersWithNotifications=new ArrayList<>();

    @Override
    public void run() {
        //usersWithNotifications=userService.getAllUsersWithNotifications();
        while (true) {
            try {
                Thread.sleep(1000);
                if(usersWithNotifications!=null) {

                    for (User user : usersWithNotifications) {
                        System.out.println(user.getUserId() + " not/");
                        if(user.getNotificationTime()== LocalTime.now()){
                            CityData notificationsCity=user.getNotificationCity();
                            WeatherData weatherData=geoWeatherProvider.getWeatherData(
                                    notificationsCity.getLat(), notificationsCity.getLon()
                            );
                            

                        }
                    }
                }
            } catch (InterruptedException e) {

            }

        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void gotUpdate(Object object) {
        System.out.println("notifier was update");
        usersWithNotifications=userService.getAllUsersWithNotifications();

    }
}
