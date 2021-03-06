package github.jdrost1818.repository;

import github.jdrost1818.model.SearchCriteria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface SearchCriteriaRepository extends CrudRepository<SearchCriteria, Long> {
}
