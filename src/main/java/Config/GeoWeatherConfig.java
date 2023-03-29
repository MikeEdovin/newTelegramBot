package Config;

import GeoWeatherPackage.IGeoWeatherProvider;
import GeoWeatherPackage.GeoWeatherProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

public class GeoWeatherConfig {
    @Bean("restTemplateBuilder")
    public RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }
    @Bean
    public IGeoWeatherProvider geoWeatherProvider(RestTemplateBuilder restTemplateBuilder){
        return new GeoWeatherProvider(restTemplateBuilder);
    }
}
