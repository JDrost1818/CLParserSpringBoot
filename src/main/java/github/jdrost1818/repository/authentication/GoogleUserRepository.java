package github.jdrost1818.repository.authentication;

import github.jdrost1818.model.authentication.GoogleUser;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface GoogleUserRepository extends CrudRepository<GoogleUser, String> {
}
