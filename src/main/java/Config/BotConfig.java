package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    String botName="EdovinWeatherBot";
    String botToken="5295256851:AAHhA46BhJUzvzIE29rpjE8KlVCsDobF-is";

    @Bean
    public Bot getWeatherBot(){
        return new WeatherBot(botName,botToken);
    }

}
