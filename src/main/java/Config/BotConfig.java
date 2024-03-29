package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import BotServices.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("config.properties")
@EnableScheduling
public class BotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String botToken;
    @Bean
    public Bot getWeatherBot(){
        return new WeatherBot(botName,botToken);
    }

    @Bean
    public Worker getWorker(){
        return new WorkerImpl();
    }

    @Bean
    public Notifier getNotifier(){return new NotifierImpl();}

}
