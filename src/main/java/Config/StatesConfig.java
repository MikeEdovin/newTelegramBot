package Config;

import States.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatesConfig {
    @Bean(name="mainState")
    public State getMainState(){
        return new MainState();
    }
    @Bean
    public StateFactory getStateFactory(){
        return new StateFactoryImpl();
    }

    @Bean(name="setCityState")
    public State getSetCityState(){return new SetCityState();}
}
