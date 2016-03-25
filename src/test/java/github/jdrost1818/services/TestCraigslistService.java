package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.data.CraigslistUrls;
import github.jdrost1818.model.SearchCriteria;
import github.jdrost1818.util.JSoupAddOn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Jake on 3/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
@WebAppConfiguration
public class TestCraigslistService {

    @Autowired
    CraigslistService craigslistService;

    private SearchCriteria fullCriteria;

    @Before
    public void init() {
        fullCriteria = new SearchCriteria();
        fullCriteria.setId(0L);
        fullCriteria.setCategory(CraigslistUrls.ALL.owner());
        fullCriteria.setCity("minneapolis");
        fullCriteria.setExclusions("Do Not Include");
        fullCriteria.setMatch("Should Match");
        fullCriteria.setMinPrice(0);
        fullCriteria.setMaxPrice(100);
        fullCriteria.setPostedToday(true);
        fullCriteria.setHasPic(true);
        fullCriteria.setSearchTitlesOnly(true);
        fullCriteria.setIncludeNearbyAreas(true);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateCraigslistUrlNullCity() {
        fullCriteria.setCity(null);
        craigslistService.createCraigslistUrl(fullCriteria, 0);
    }

    @Test(expected=IllegalArgumentException.class)
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all()) + "&query=";
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), fullCriteria.getCategory()) + "&query=";
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
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
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), CraigslistUrls.ALL.all());
        expectedUrl += "&query=&searchNearby=1";
        String actualUrl = craigslistService.createCraigslistUrl(criteria, 0);
        assertEquals(actualUrl, expectedUrl);

        // Make sure what we created is valid to Craigslist paths
        assertNotNull(JSoupAddOn.connect(actualUrl));
    }

    @Test
    public void testCreateCraigslistUrlAll() {
        // Ensure the url is proper
        String expectedUrl = String.format(CraigslistService.BASE_URL, fullCriteria.getCity(), fullCriteria.getCategory());
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


}
