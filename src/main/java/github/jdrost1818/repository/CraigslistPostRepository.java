package github.jdrost1818.repository;

import github.jdrost1818.model.craigslist.CraigslistPost;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Component
public class CraigslistPostRepository {

    private HashMap<Long, CraigslistPost> db = new HashMap<>();

    public void save(CraigslistPost post) {
        db.put(post.getId(), post);
    }

    public CraigslistPost findOne(Long postId) {
        if (db.containsKey(postId))
            return db.get(postId);
        return null;
    }
}
