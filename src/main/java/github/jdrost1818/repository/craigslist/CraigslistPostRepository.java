package github.jdrost1818.repository.craigslist;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import github.jdrost1818.model.craigslist.CraigslistPost;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Repository
public interface CraigslistPostRepository extends CrudRepository<CraigslistPost, String> {
}

