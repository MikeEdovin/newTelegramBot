package Service;

import Entities.CityData;
import Entities.WeatherId;

import java.util.Optional;

public interface ICityService {
    CityData save(CityData cityData);
    Optional<CityData> getCityById(WeatherId weatherId);

}
