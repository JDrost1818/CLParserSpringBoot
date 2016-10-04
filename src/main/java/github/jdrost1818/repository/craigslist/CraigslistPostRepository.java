package github.jdrost1818.repository.craigslist;

import github.jdrost1818.model.craigslist.CraigslistPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface CraigslistPostRepository extends CrudRepository<CraigslistPost, String> {
}

