package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;

public interface GeoWeatherProvider {
    WeatherData getWeatherData(Double latitude, Double longitude);
    CityData[] getCityData(String city);
}
