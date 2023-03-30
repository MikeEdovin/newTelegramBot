package Config;

import Handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class HandlersConfig {
    @Bean(name="defaultHandler")
    public IHandler getDefaultHandler(){
        return new DefaultHandler();
    }
    @Bean(name="systemHandler")
    public IHandler getSystemHandler(){
        return new SystemHandler();
    }
    @Bean(name="weatherHandler")
    public IHandler getWeatherHandler(){
        return new WeatherHandler();
    }
    @Bean
    public IHandlerFactory getHandlerFactory(){
        return new HandlerFactory();
    }

}
