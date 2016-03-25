package github.jdrost1818.repository;

import github.jdrost1818.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, String> {
}
