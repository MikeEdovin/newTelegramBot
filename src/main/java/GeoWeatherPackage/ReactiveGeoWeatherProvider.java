package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ReactiveGeoWeatherProvider {
    Mono<WeatherData> getWeatherData(double latitude, double longitude);
    Flux<CityData> getCityData(String city);
    Mono<CityData> getCityData(double latitude, double longitude);
}
