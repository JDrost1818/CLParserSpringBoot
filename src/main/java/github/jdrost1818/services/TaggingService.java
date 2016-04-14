package github.jdrost1818.services;

import github.jdrost1818.model.Tag;
import github.jdrost1818.model.TagRelation;
import github.jdrost1818.repository.TagRelationRepository;
import github.jdrost1818.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * In charge of generating, caching, and saving tags for Strings.
 *
 * Created by JAD0911 on 4/14/2016.
 */
@Component
public class TaggingService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagRelationRepository tagRelationRepository;

    /**
     * Gets all tags that can be applied to the words given
     *
     * @param words words in which to look for tag words
     * @return list of tags which have a relation in the words
     */
    public List<Tag> getTags(String words) {
        String[] wordList = words.split(" ");
        List<Tag> tagList = new ArrayList<>();
        for (String word : wordList) {
            TagRelation foundTag = tagRelationRepository.findOne(word);
            if (foundTag != null) {
                tagList.add(foundTag.getTag());
            }
        }
        return tagList;
    }

    /**
     * Adds a relation between word(s) and a tag to be used later
     * for auto-tagging. Also saves the Tag if it does not already
     * exist in the database;
     *
     * @param words   String which will be related to the tag
     * @param tagLine Tag to which the words will be related
     */
    public void addTagRelation(String words, String tagLine) {
        if (words != null && !"".equals(words) && tagLine != null && !"".equals(tagLine)) {
            Tag tag = tagRepository.findByTitleIgnoreCase(tagLine);
            if (tag == null) {
                tag = new Tag(tagLine);
                tagRepository.save(tag);
            }
            tagRelationRepository.save(new TagRelation(words, tag));
        }
    }

    /**
     * Essentially extends the save functionality to this component
     * to keep all tag concerns centrally located.
     *
     * @param tag Tag to save
     */
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    /**
     * Essentially extends the save functionality to this component
     * to keep all tag concerns centrally located.
     *
     * @param tags Tags to save
     */
    public void saveTags(List<Tag> tags) {
        tagRepository.save(tags);
    }

}
