package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ReactiveGeoWeatherProviderImpl implements ReactiveGeoWeatherProvider{
    //@Autowired
    private WebClient webClient;
    @Value("${weather.APP_ID}") private  String APP_ID;
    static final Logger logger= LoggerFactory.getLogger(GeoWeatherProviderImpl.class);

    @Override
    public Mono<WeatherData> getWeatherData(double latitude, double longitude) {
        logger.info("requested weatherData for lat "+latitude+" longitude "+longitude);
        final String URL_API = "https://api.openweathermap.org/data/2.5/onecall?";
        String url = URL_API + "lat=" + latitude + "&lon=" + longitude
                + "&exclude=minutely,hourly" + "&appid=" + APP_ID + "&units=metric";
        return webClient.get().uri(url).retrieve().bodyToMono(WeatherData.class);
    }

    @Override
    public Flux<CityData> getCityData(String city) {
        logger.info("requested cityData for "+city);
        final String URL_API = "http://api.openweathermap.org/geo/1.0/direct?q=";
        String url = URL_API + city + "&limit=3&appid=" + APP_ID;
        return webClient.get().uri(url).retrieve().bodyToFlux(CityData.class);
    }

    @Override
    public Mono<CityData> getCityData(double latitude, double longitude) {
        logger.info("requested cityData for lat "+latitude+" &lon "+longitude);
        final String URL_API = "http://api.openweathermap.org/geo/1.0/reverse?lat=";
        String url = URL_API + latitude + "&lon=" + longitude + "&limit=1&appid=" + APP_ID;
        return webClient.get().uri(url).retrieve().bodyToMono(CityData.class);
    }
}
