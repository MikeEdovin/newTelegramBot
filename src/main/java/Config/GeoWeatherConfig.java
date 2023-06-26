package Config;

import GeoWeatherPackage.GeoWeatherProvider;
import GeoWeatherPackage.GeoWeatherProviderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("config.properties")
public class GeoWeatherConfig {

    @Value("${weather.APP_ID}") String weatherMapId;
    @Bean("restTemplateBuilder")
    public RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }
    @Bean
    public GeoWeatherProvider geoWeatherProvider(RestTemplateBuilder restTemplateBuilder){
        return new GeoWeatherProviderImpl(restTemplateBuilder,weatherMapId);
    }
}
