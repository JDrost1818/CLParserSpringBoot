package github.jdrost1818.services;

import github.jdrost1818.model.User;
import github.jdrost1818.model.craigslist.CraigslistPost;
import github.jdrost1818.model.craigslist.CraigslistSearchCriteria;
import github.jdrost1818.repository.CraigslistPostRepository;
import github.jdrost1818.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Component
public class CraigslistService {

    @Autowired
    CraigslistPostRepository craigslistPostRepository;

    @Autowired
    UserRepository userRepository;

    // This may eventually be customizable by user
    public static final String BASE_URL = "https://minneapolis.craigslist.org/";

    public CraigslistService() {
        if (craigslistPostRepository == null) {
            craigslistPostRepository = new CraigslistPostRepository();
        }
    }

    /**
     * Searches for all posts that are matched by the {@link CraigslistSearchCriteria}
     * and for which the user has not seen.
     *
     * @param searchCriteria criteria for which to search
     * @param user           user that is making the search
     * @return list of all posts that meet the criteria and have not been seen by the user
     */
    public List<CraigslistPost> search(CraigslistSearchCriteria searchCriteria, User user) {
        return null;
    }

}
