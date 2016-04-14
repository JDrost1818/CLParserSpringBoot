package github.jdrost1818.repository;

import github.jdrost1818.model.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by JAD0911 on 4/14/2016.
 */
public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findByTitleIgnoreCase(String title);
}
