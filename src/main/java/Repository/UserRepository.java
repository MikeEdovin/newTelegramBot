package Repository;

import Entities.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<User,Long> {


}
