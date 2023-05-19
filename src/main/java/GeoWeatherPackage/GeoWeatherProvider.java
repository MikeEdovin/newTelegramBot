package GeoWeatherPackage;

import Entities.CityData;
import Entities.WeatherData;

public interface GeoWeatherProvider {
    WeatherData getWeatherData(double latitude, double longitude);
    CityData[] getCityData(String city);
    CityData getCityData(double latitude, double longitude);
}
