package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import github.jdrost1818.model.Item;
import github.jdrost1818.repository.ItemRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestPricingService {

    @Autowired
    private PricingService pricingService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCalcPriceItemDNE() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.calcPrice("this item will not exist kfajsl;dkja;slkdlkasdf"));
    }

    @Test
    public void testCalcPriceItemNull() {
        Item item = null;
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.calcPrice(item));
    }

    @Test
    public void testCalcPriceItemEmptyString() {
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.calcPrice(""));
    }

    @Test
    public void testCalcPriceItemExists() {
        // Since prices don't stay the same, I'm going to assume
        // that if we get two different prices for two different
        // products, it will be getting the prices correctly
        BigDecimal lowPrice = pricingService.calcPrice("penny");
        BigDecimal highPrice = pricingService.calcPrice("hundred dollar");

        assertNotEquals(lowPrice, highPrice);
    }

    @Test
    public void testCalcPriceNullInput() {
        Item item = null;
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.calcPrice(item));
    }

    @Test
    public void testCalcPriceGetsCached() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10"));
        testItem.setDateCached(new Date(0));
        itemRepository.save(testItem);

        // Ensure the service doesn't get a cached price
        BigDecimal price = pricingService.calcPrice(testItem);
        assertNotEquals(price, testItem.getPrice());

        // Ensure the new price is cached
        assertEquals(price.longValue(), itemRepository.findOne(testItem.getName()).getPrice().longValue());
    }

    @Test
    public void testCalcPriceGetsCachedPrice() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10.00"));
        testItem.setDateCached(new Date());
        itemRepository.save(testItem);

        assertEquals(testItem.getPrice(), pricingService.calcPrice(testItem.getName()));
    }

    @Test
    public void testCalcPriceItemNotInDB() {
        Item testItem = new Item("test");
        testItem.setPrice(new BigDecimal("-10"));
        testItem.setDateCached(new Date(0));
        itemRepository.delete(testItem);

        // Ensure the service doesn't get a cached price
        BigDecimal price = pricingService.calcPrice(testItem.getName());
        assertNotEquals(price, testItem.getPrice());
    }

    @Test
    public void testCalcPriceListNull() {
        List<Item> items = null;
        assertEquals(PricingService.NO_PRICING_DATA, pricingService.calcPrice(items));
    }

    @Test
    public void testCalcPriceList() {
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("item1");
        item1.setDateCached(new Date());
        item1.setPrice(BigDecimal.valueOf(10));

        Item item2 = new Item("item2");
        item2.setDateCached(new Date());
        item2.setPrice(BigDecimal.valueOf(15));

        BigDecimal total = item1.getPrice().add(item2.getPrice());

        itemRepository.save(item1);
        itemRepository.save(item2);

        items.add(item1);
        items.add(item2);

        assertEquals(total, pricingService.calcPrice(items));
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

    @After
    public void cleanUp() {
        itemRepository.deleteAll();
    }
}
