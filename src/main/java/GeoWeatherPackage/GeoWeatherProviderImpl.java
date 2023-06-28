package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

public class GeoWeatherProviderImpl implements GeoWeatherProvider {
   // @Autowired
  //  private CacheManager cacheManager;
    private final RestTemplate restTemplate;
    private final String APP_ID;

    public GeoWeatherProviderImpl(RestTemplateBuilder restTemplateBuilder, String weatherMapId) {
        this.restTemplate = restTemplateBuilder.build();
        this.APP_ID=weatherMapId;
    }
    @Override
    @Async
    public CompletableFuture<WeatherData> getWeatherDataAsync(double latitude, double longitude) {
        System.out.println("geo Thread weatherData"+Thread.currentThread().getName());
        final String URL_API = "https://api.openweathermap.org/data/2.5/onecall?";
        String url = URL_API + "lat=" + latitude + "&lon=" + longitude
                + "&exclude=minutely,hourly" + "&appid=" + APP_ID + "&units=metric";
        return CompletableFuture.completedFuture(this.restTemplate.getForObject(url, WeatherData.class));
    }
    @Override
    @Async
    public CompletableFuture<CityData[]> getCityDataAsync(String city) {
        System.out.println("geo Thread getCityData "+Thread.currentThread().getName());
        final String URL_API = "http://api.openweathermap.org/geo/1.0/direct?q=";
        String url = URL_API + city + ",ru_RU" + "&limit=3&appid=" + APP_ID;
        return CompletableFuture.completedFuture(this.restTemplate.getForObject(url,CityData[].class));
    }

    @Override
    @Async
    public CompletableFuture<CityData> getCityDataAsync(double latitude, double longitude) {
        System.out.println("geo Thread location "+Thread.currentThread().getName());
        final String URL_API = "http://api.openweathermap.org/geo/1.0/reverse?lat=";
        String url = URL_API + latitude + "&lon=" + longitude + "&limit=1&appid=" + APP_ID;
        return CompletableFuture.completedFuture(this.restTemplate.getForObject(url,CityData[].class)[0]);
    }


}
