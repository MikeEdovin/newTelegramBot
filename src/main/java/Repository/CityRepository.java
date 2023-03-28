package Repository;

import Entities.CityData;
import Entities.WeatherId;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<CityData, WeatherId> {
}
