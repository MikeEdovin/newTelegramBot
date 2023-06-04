package Service;

import Entities.CityData;
import Entities.CityId;

import java.util.Optional;

public interface CityService {
    CityData save(CityData cityData);
    Optional<CityData> getCityById(CityId cityId);



}
