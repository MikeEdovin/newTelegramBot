package Config;

import GeoWeatherPackage.GeoWeatherProvider;
import GeoWeatherPackage.GeoWeatherProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeoWeatherConfig {

    @Bean("restTemplate")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean("geoWeatherProvider")
    public GeoWeatherProvider geoWeatherProvider(){
        return new GeoWeatherProviderImpl();
    }

}
