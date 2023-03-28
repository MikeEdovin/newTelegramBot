package Service;

import Entities.WeatherData;
import Entities.WeatherId;
import Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class WeatherServiceImpl implements WeatherService{
    @Autowired
    WeatherRepository repository;
    @Override
    public WeatherData save(WeatherData weatherData) {
        return repository.save(weatherData);
    }

    @Override
    public Optional<WeatherData> getWeatherDataById(WeatherId weatherId) {
        return repository.findById(weatherId);
    }
}
