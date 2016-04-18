package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.Tag;
import github.jdrost1818.repository.TagRelationRepository;
import github.jdrost1818.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by JAD0911 on 4/14/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestTaggingService {

    @Autowired
    TaggingService taggingService;

    private Tag iPhone;
    private Tag macbookAir;
    private Tag black;
    private Tag white;

    @Before
    public void init() {
        iPhone = new Tag("iPhone");
        macbookAir = new Tag("MacBook Air");
        black = new Tag("Black");
        white = new Tag("White");

        List<Tag> tagList = new ArrayList<>();
        tagList.add(iPhone);
        tagList.add(macbookAir);
        tagList.add(black);
        tagList.add(white);

        taggingService.deleteAll();
        taggingService.saveTags(tagList);
    }

    @Test
    public void testGetTagsSingleWord() {
        List<Tag> tagsFound = taggingService.getTags("iphone");
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(iPhone);

        ensureAllTagsPresent(tagsFound, expectedTags);
    }

    private void ensureAllTagsPresent(List<Tag> foundTags, List<Tag> expectedTags) {
        String errorMsg = "";
        for (Tag tag1 : foundTags) {
            for (Tag tag2 : expectedTags) {
                if (tag1.getTitle().equalsIgnoreCase(tag2.getTitle())) {
                    expectedTags.remove(tag2);
                    break;
                }
            }
        }
        if (!expectedTags.isEmpty()) {
            for (Tag notFoundTag : expectedTags) {
                errorMsg += String.format("%s - not found\n", notFoundTag.getTitle());
            }
            throw new IllegalStateException(errorMsg);
        }
    }

    @Test
    public void testGetTagsMultipleWords() {
        List<Tag> tagsFound = taggingService.getTags("super clean black, 32gb, iphone");
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(black);
        expectedTags.add(iPhone);

        ensureAllTagsPresent(tagsFound, expectedTags);
    }

    @Test
    public void testGetTwoWordTag() {
        List<Tag> tagsFound = taggingService.getTags("super clean black, 128gb MACBOOK AiR");
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(black);
        expectedTags.add(macbookAir);

        ensureAllTagsPresent(tagsFound, expectedTags);
    }

    @Test
    public void testDelete() {
        List<Tag> tagsFound = taggingService.getTags("iphone");
        assertEquals(iPhone.getTitle(), tagsFound.get(0).getTitle());

        taggingService.delete(iPhone);
        tagsFound = taggingService.getTags("iphone");
        assertEquals(0, tagsFound.size());
    }

}
