package Service;

import Entities.CityData;

import java.util.concurrent.CompletableFuture;

public interface CityService {
    CompletableFuture<CityData> save(CityData cityData);
}
