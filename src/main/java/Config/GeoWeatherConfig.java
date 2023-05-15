package Config;

import GeoWeatherPackage.GeoWeatherProvider;
import GeoWeatherPackage.GeoWeatherProviderImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

public class GeoWeatherConfig {
    @Bean("restTemplateBuilder")
    public RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }
    @Bean
    public GeoWeatherProvider geoWeatherProvider(RestTemplateBuilder restTemplateBuilder){
        return new GeoWeatherProviderImpl(restTemplateBuilder);
    }
}
