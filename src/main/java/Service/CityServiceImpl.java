package Service;

import Entities.CityData;
import Repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

public class CityServiceImpl implements CityService{
    @Autowired
    CityRepository repository;

    @Override
    public CompletableFuture<CityData> save(CityData cityData) {
        return CompletableFuture.completedFuture(repository.save(cityData));
    }
}
