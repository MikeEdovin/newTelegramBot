package Repository;

import Entities.CityData;
import Entities.CityId;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<CityData, CityId> {
}
