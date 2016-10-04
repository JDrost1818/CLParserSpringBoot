package github.jdrost1818.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import github.jdrost1818.model.Item;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface ItemRepository extends CrudRepository<Item, String> {
}
