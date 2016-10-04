package github.jdrost1818.repository;

import github.jdrost1818.model.authentication.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
