package BotServices;

import BotPackage.Bot;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.ReactiveGeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.ReactiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotifierImpl implements Notifier {
    @Autowired
    //UserService userService;
    ReactiveUserService userService;
    @Autowired
    //GeoWeatherProvider geoWeatherProvider;
    ReactiveGeoWeatherProvider geoWeatherProvider;
    @Autowired
    Bot bot;
    final static Logger logger= LoggerFactory.getLogger(NotifierImpl.class);
    private List<User> usersWithNotifications = new ArrayList<>();
    @Override
    public void gotNotifListUpdate(User user) {
        usersWithNotifications.clear();
        /*
        try {
            usersWithNotifications = userService.getAllUsersWithNotificationsAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.warn(e.getMessage());
        }

         */
        userService.getAllUsersWithNotifications().subscribe(u->{
            usersWithNotifications.add(u);
        });
    }
    @Override
    @Async
    @Scheduled(fixedRate = 10000)
    public void sendNotifications() {
        if (usersWithNotifications != null) {
            for (User user : usersWithNotifications) {
                CityData notificationsCity = user.getNotificationCity();
                ZonedDateTime now;
                if(notificationsCity.getTimezone()!=null) {
                    now = ZonedDateTime.now(ZoneId.of(notificationsCity.getTimezone()));
                }
                else{
                    now=ZonedDateTime.now(ZoneId.systemDefault());
                }
                LocalTime notifTime=user.getNotificationTime();
                logger.info("city "+notificationsCity.getName()+" now "+now.getHour()+" "+now.getMinute()+" notifTime "
                        +notifTime.getHour()+" "+notifTime.getMinute());
                if (notifTime.getHour() == now.getHour()
                &&notifTime.getMinute()==now.getMinute()) {
                    geoWeatherProvider.getWeatherData(notificationsCity.getLat(), notificationsCity.getLon())
                            .subscribe(w->{
                                try {
                                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                            .setForecastText(w, notificationsCity, 1).build().getSendMessage());
                                }
                                catch (TelegramApiException e){
                                    logger.warn(e.getMessage());
                                }
                            });
                    /*
                    CompletableFuture<WeatherData> futureWeatherData =
                            geoWeatherProvider.getWeatherDataAsync(
                                    notificationsCity.getLat(), notificationsCity.getLon());
                    try {
                        WeatherData weatherData=futureWeatherData.get();
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .setForecastText(weatherData, notificationsCity, 1).build().getSendMessage());
                    } catch (TelegramApiException|InterruptedException|ExecutionException e) {
                        logger.warn(e.getMessage());
                    }

                     */

                }
            }
        }

    }
}


