package BotServices;

import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import Service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.FixedRateTask;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NotifierImpl implements Notifier{
    @Autowired
    UserService userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;

    private List<User> usersWithNotifications;


    @Override
    public void gotNotifListUpdate(User user) {
        System.out.println("notifier was update");
        try {
            usersWithNotifications = userService.getAllUsersWithNotificationsAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
/*
        while (true) {
            try {
                Thread.sleep(1000);
                if(usersWithNotifications!=null) {
                    for (User user : usersWithNotifications) {
                        System.out.println(user.getUserId() + " not/");
                        if(user.getNotificationTime()== LocalTime.now()){
                            CityData notificationsCity=user.getNotificationCity();
                            CompletableFuture<WeatherData> futureWeatherData=
                            geoWeatherProvider.getWeatherDataAsync(
                                    notificationsCity.getLat(), notificationsCity.getLon());
                                try {
                                    WeatherData weatherData = futureWeatherData.get();
                                }catch (InterruptedException| ExecutionException e){
                                    e.printStackTrace();
                                    //add service temporary unavailable message
                            }


                        }
                    }
                }
            } catch (InterruptedException e) {

            }

        }

 */
    }

    @Override
    public void stop() {

    }

    @Override
    @Async
    @Scheduled(fixedRate = 1000)
    public void sendNotifications() {
        for (User user : usersWithNotifications) {
            System.out.println(user.getUserId() + " not/");
            if (user.getNotificationTime() == LocalTime.now()) {
                CityData notificationsCity = user.getNotificationCity();
                CompletableFuture<WeatherData> futureWeatherData =
                        geoWeatherProvider.getWeatherDataAsync(
                                notificationsCity.getLat(), notificationsCity.getLon());
                try {
                    WeatherData weatherData = futureWeatherData.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    //add service temporary unavailable message
                }
            }
        }

    }

}
