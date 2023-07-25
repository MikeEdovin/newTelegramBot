package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;


import java.util.concurrent.CompletableFuture;

public interface GeoWeatherProvider {
    CompletableFuture<WeatherData> getWeatherDataAsync(double latitude, double longitude);

    CompletableFuture<CityData[]> getCityDataAsync(String city);

    CompletableFuture<CityData> getCityDataAsync(double latitude, double longitude);
}


