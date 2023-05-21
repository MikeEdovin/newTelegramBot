package Service;

import Entities.CityData;
import Entities.WeatherId;

import java.util.Optional;

public interface CityService {
    CityData save(CityData cityData);
    Optional<CityData> getCityById(WeatherId weatherId);



}
