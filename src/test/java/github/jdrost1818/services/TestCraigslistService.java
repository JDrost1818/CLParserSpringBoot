package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.data.CraigslistCategory;
import github.jdrost1818.data.CraigslistConstants;
import github.jdrost1818.model.SearchCriteria;
import github.jdrost1818.model.craigslist.CraigslistPost;
import github.jdrost1818.repository.craigslist.CraigslistPostRepository;
import github.jdrost1818.util.JSoupAddOn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestCraigslistService {

    @Autowired
    private CraigslistService craigslistService;

    @Autowired
    private CraigslistPostRepository craigslistPostRepository;

    private static final String CWD = System.getProperty("user.dir");

    private SearchCriteria fullCriteria;
    private SearchCriteria smallResultCriteria;

    private String baseUrl;
    private Document doc;
    private Element postHtml;
    private CraigslistPost post;

    @Before
    public void init() throws IOException {
        fullCriteria = new SearchCriteria();
        fullCriteria.setId(0L);
        fullCriteria.setCategory(CraigslistCategory.ALL.owner());
        fullCriteria.setCity("minneapolis");
        fullCriteria.setExclusions("Do Not Include");
        fullCriteria.setMatch("Should Match");
        fullCriteria.setMinPrice(0);
        fullCriteria.setMaxPrice(1000);
        fullCriteria.setPostedToday(true);
        fullCriteria.setHasPic(true);
        fullCriteria.setSearchTitlesOnly(true);
        fullCriteria.setIncludeNearbyAreas(true);

        // This will usually return ~3 pages depending on the time of day
        // which allows for testing to not be that long for when we
        // actually have to hit Craigslist for testing
        smallResultCriteria = new SearchCriteria();
        smallResultCriteria.setId(1L);
        smallResultCriteria.setCity("minneapolis");
        smallResultCriteria.setCategory(CraigslistCategory.ALL.owner());
        smallResultCriteria.setHasPic(true);
        smallResultCriteria.setPostedToday(true);
        smallResultCriteria.setMatch("computer");

        // This is the representation of the post found in JSoup.html
        baseUrl = CraigslistConstants.getBaseUrl("minneapolis");
        doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8");
        postHtml = doc.select(CraigslistConstants.POST_WRAPPER_TAG).select(CraigslistConstants.POST_TAG).get(0);
        post = CraigslistPost.parsePost(postHtml, baseUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCraigslistUrlNullCity() {
        fullCriteria.setCity(null);
        craigslistService.createCraigslistUrl(fullCriteria, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCraigslistUrlEmptyCity() {
        fullCriteria.setCity("");
        craigslistService.createCraigslistUrl(fullCriteria, 0);
    }

    @Test
    public void testCreateCraigslistUrlOnlyCity() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "s=&query=&min_price=&max_price=&srchType=&hasPic=&postedToday=&searchNearby=";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistFullCriteria() {
        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), fullCriteria.getCategory());
        expectedUrl += "s=10&query=Should+Match+-Do+-Not+-Include&min_price=0&max_price=1000&srchType=T&hasPic=1&postedToday=1&searchNearby=1";
        String actualUrl = craigslistService.createCraigslistUrl(fullCriteria, 10);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testLoadPost() {
        CraigslistPost foundPost = craigslistService.loadPost(postHtml, baseUrl);
        assertTrue(post.deepEquals(foundPost));
    }

    @Test
    public void testLoadPostCacheOutOfDate() {
        // Sets the post to outdated in db
        Integer originalPrice = post.getPrice();
        post.setDateCached(new Date(0));
        post.setPrice(-100);
        craigslistPostRepository.save(post);

        CraigslistPost foundPost = craigslistService.loadPost(postHtml, baseUrl);

        post.setPrice(originalPrice);

        // This means that it parsed the file instead of just pulling out of db
        assertTrue(post.deepEquals(foundPost));
    }

    @Test
    public void testLoadPostInvalidPost() {
        Element illegalHtml = doc.select(CraigslistConstants.POST_WRAPPER_TAG).select(CraigslistConstants.POST_TAG).get(1);
        CraigslistPost foundPost = craigslistService.loadPost(illegalHtml, baseUrl);
        assertNull(foundPost);
    }

    @Test
    public void testParsePageAllNull() {
        List<CraigslistPost> foundPosts = craigslistService.parsePage(null, null, null);
        assertEquals(0, foundPosts.size());
    }

    @Test
    public void testParsePageDateNull() {
        List<CraigslistPost> foundPosts = craigslistService.parsePage(null, doc, baseUrl);
        // This ensures it parses correctly but
        // also that doesn't add malformed posts
        assertEquals(1, foundPosts.size());
        assertTrue(foundPosts.get(0).deepEquals(post));
    }

    @Test
    public void testParsePageDocNull() {
        List<CraigslistPost> foundPosts = craigslistService.parsePage(new Date(0), null, baseUrl);
        assertEquals(0, foundPosts.size());
    }

    @Test
    public void testParsePageBaseUrlNull() {
        List<CraigslistPost> foundPosts = craigslistService.parsePage(new Date(0), doc, null);
        assertEquals(1, foundPosts.size());
    }

    @Test
    public void testParsePageNoNewPosts() {
        List<CraigslistPost> foundPosts = craigslistService.parsePage(new Date(), doc, baseUrl);
        assertEquals(0, foundPosts.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchCityNull() {
        smallResultCriteria.setCity(null);
        craigslistService.search(smallResultCriteria, new Date(0));
    }

    @Test
    public void testSearch() {
        List<CraigslistPost> foundPosts = craigslistService.search(smallResultCriteria, new Date(0));
        assertFalse(foundPosts.isEmpty());
    }

    @Test
    public void testSearchNulls() {
        List<CraigslistPost> foundPosts = craigslistService.search(null, null);
        assertEquals(0, foundPosts.size());
    }

}
