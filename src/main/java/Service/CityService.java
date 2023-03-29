package Service;

import Entities.CityData;
import Entities.WeatherId;
import Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CityService implements ICityService {
    @Autowired
    CityRepository repository;
    @Override
    public CityData save(CityData cityData) {
        return repository.save(cityData);
    }

    @Override
    public Optional<CityData> getCityById(WeatherId weatherId) {
        return repository.findById(weatherId);
    }
}
