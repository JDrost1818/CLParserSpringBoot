package github.jdrost1818.repository.authentication;

import github.jdrost1818.model.authentication.FacebookUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface FacebookUserRepository extends CrudRepository<FacebookUser, String> {
}
