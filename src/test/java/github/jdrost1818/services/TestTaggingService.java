package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.Tag;
import github.jdrost1818.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

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

    @Autowired
    TagRepository tagRepository;

    Tag iPhone;
    Tag black;
    Tag white;

    @Before
    public void init() {
        iPhone = new Tag("iPhone");
        black = new Tag("Black");
        white = new Tag("White");

        tagRepository.deleteAll();
        tagRepository.save(iPhone);
        tagRepository.save(black);
        tagRepository.save(white);
    }

    @Test
    public void testGetTagsSingleWord() {
        List<Tag> tagsFound = taggingService.getTags("iphone");

        assertEquals(1, tagsFound.size());
        assertEquals(tagsFound.get(0).getTitle(), iPhone.getTitle());
    }

}
