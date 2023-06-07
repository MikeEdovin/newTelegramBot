package Config;

import States.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatesConfig {

    @Bean
    public StateFactory getStateFactory(){
        return new StateFactoryImpl();
    }
    @Bean(name="mainState")
    public State getMainState(){
        return new MainState();
    }
    @Bean(name="setCityState")
    public State getSetCityState(){return new SetCityState();}
    @Bean(name="newInputState")
    public State getNewInputState(){return new NewInputState();}
    @Bean(name="notificationsState")
    public State getNotificationsState(){return new NotificationsState();}
}
