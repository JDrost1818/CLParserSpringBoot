package github.jdrost1818.repository.craigslist;

import github.jdrost1818.model.craigslist.CraigslistPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Repository
public interface CraigslistPostRepository extends CrudRepository<CraigslistPost, String> {
}

