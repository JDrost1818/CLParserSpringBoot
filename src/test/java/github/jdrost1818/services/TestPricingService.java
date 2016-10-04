package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.Item;
import github.jdrost1818.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestPricingService {

    @Autowired
    private PricingService pricingService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testGetPriceItemDNE() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.getPrice("this item will not exist kfajsl;dkja;slkdlkasdf"));
    }

    @Test
    public void testGetPriceItemNull() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.getPrice(null));
    }

    @Test
    public void testGetPriceItemEmptyString() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.getPrice(""));
    }

    @Test
    public void testGetPriceItemExists() {
        // Since prices don't stay the same, I'm going to assume
        // that if we get two different prices for two different
        // products, it will be getting the prices correctly
        BigDecimal lowPrice = pricingService.getPrice("penny");
        BigDecimal highPrice = pricingService.getPrice("hundred dollar");

        assertNotEquals(lowPrice, highPrice);
    }

    @Test
    public void testGetPriceNullInput() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.getPrice(null));
    }

    @Test
    public void testGetPriceGetsCached() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10"));
        testItem.setDateCached(LocalDateTime.now());
        itemRepository.save(testItem);

        // Ensure the service doesn't get a cached price
        BigDecimal price = pricingService.getPrice(testItem.getName());
        assertNotEquals(price, testItem.getPrice());

        // Ensure the new price is cached
        assertEquals(price, itemRepository.findOne("test").getPrice());
    }

    @Test
    public void testGetPriceGetsCachedPrice() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10.00"));
        testItem.setDateCached(LocalDateTime.now());
        itemRepository.save(testItem);

        assertEquals(testItem.getPrice(), pricingService.getPrice(testItem.getName()));
    }

    @Test
    public void testGetPriceItemNotInDB() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10"));
        testItem.setDateCached(LocalDateTime.MIN);
        itemRepository.delete(testItem);

        // Ensure the service doesn't get a cached price
        BigDecimal price = pricingService.getPrice(testItem.getName());
        assertNotEquals(price, testItem.getPrice());
    }

    @Test
    public void testQueryToUrl() {
        String query = "iPad Mini 100 &";
        String expected = PricingService.BASE_URL + "iPad+Mini+100+&" + PricingService.URL_SUFFIX;
        assertEquals(expected, pricingService.queryToUrl(query));
    }

    @Test
    public void testQueryToUrlNullInput() {
        assertEquals(PricingService.NULL_URL, pricingService.queryToUrl(null));
    }

    @Test
    public void testQueryToUrlEmptyString() {
        assertEquals(PricingService.NULL_URL, pricingService.queryToUrl(""));
    }
}
