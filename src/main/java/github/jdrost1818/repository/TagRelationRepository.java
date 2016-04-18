package github.jdrost1818.repository;

import github.jdrost1818.model.Tag;
import github.jdrost1818.model.TagRelation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by JAD0911 on 4/14/2016.
 */
public interface TagRelationRepository extends CrudRepository<TagRelation, String> {

    List<TagRelation> findAllByTag(Tag tag);

}
