package github.jdrost1818.repository;

import github.jdrost1818.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by JAD0911 on 3/24/2016.
 * <p>
 * Repository to get and store {@link User} objects from/to the database
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
