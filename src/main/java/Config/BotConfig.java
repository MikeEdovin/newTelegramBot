package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import BotServices.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableAsync
public class BotConfig {
    String botName="EdovinWeatherBot";
    String botToken="5295256851:AAHhA46BhJUzvzIE29rpjE8KlVCsDobF-is";

    @Bean
    public Bot getWeatherBot(){
        return new WeatherBot(botName,botToken);
    }
    @Bean
    public MessageReceiver getMessageReceiver(){
        return new MessageReceiverImpl();
    }
    @Bean
    public MessageSender getMessageSender(){
        return new MessageSenderImpl();
    }
    @Bean
    public Notifier getNotifier(){return new NotifierImpl();}

}
