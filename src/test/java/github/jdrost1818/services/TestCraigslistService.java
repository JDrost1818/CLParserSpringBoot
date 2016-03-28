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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jake on 3/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
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
        fullCriteria.setMaxPrice(100);
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
        post = new CraigslistPost(postHtml, baseUrl);
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

        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all()) + "&query=";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndCat() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setCategory(fullCriteria.getCategory());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), fullCriteria.getCategory()) + "&query=";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndMinPrice() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setMinPrice(fullCriteria.getMinPrice());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&minAsk=" + fullCriteria.getMinPrice();
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndMaxPrice() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setMaxPrice(fullCriteria.getMaxPrice());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&maxAsk=" + fullCriteria.getMaxPrice();
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndStartResult() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "s=100&query=";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 100);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndMatch() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setMatch(fullCriteria.getMatch());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=" + fullCriteria.getMatch().replace(" ", "+");
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(expectedUrl, actualUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndExclusions() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setExclusions(fullCriteria.getExclusions());

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=" + fullCriteria.getExclusions().replace(" ", "+-");
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndIsSearchTitlesOnly() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setSearchTitlesOnly(true);

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&srchType=T";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndHasPic() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setHasPic(true);

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&hasPic=1";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndIsPostedToday() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setPostedToday(true);

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&postedToday=1";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlBaseAndSearchNearby() {
        // Custom criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity(fullCriteria.getCity());
        criteria.setIncludeNearbyAreas(true);

        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), CraigslistCategory.ALL.all());
        expectedUrl += "&query=&searchNearby=1";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlAll() {
        // Ensure the url is proper
        String expectedUrl = CraigslistConstants.getBaseUrlSearchUrl(fullCriteria.getCity(), fullCriteria.getCategory());
        expectedUrl += String.format("s=%d&query=%s%s&minAsk=%d&maxAsk=%d&srchType=T&hasPic=1&postedToday=1&searchNearby=1",
                100,
                fullCriteria.getMatch().replace(" ", "+"),
                fullCriteria.getExclusions().replace(" ", "+-"),
                fullCriteria.getMinPrice(),
                fullCriteria.getMaxPrice());
        String actualUrl = craigslistService.createCraigslistUrl(fullCriteria, 100);
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
        post.setDateCached(new Date());
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
