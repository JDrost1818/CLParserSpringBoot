package github.jdrost1818.repository;

import github.jdrost1818.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
