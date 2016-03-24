package github.jdrost1818.repository;

import github.jdrost1818.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Component
public class UserRepository {

    private HashMap<Long, User> db = new HashMap<>();

    public void save(User user) {
        db.put(user.getId(), user);
    }

    public User findOne(Long userId) {
        if (db.containsKey(userId))
            return db.get(userId);
        return null;
    }
}
