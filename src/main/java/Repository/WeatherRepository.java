package Repository;

import Entities.WeatherData;
import Entities.WeatherId;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<WeatherData, WeatherId> {
}
