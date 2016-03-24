package github.jdrost1818.services;

import github.jdrost1818.ClparserServiceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by JAD0911 on 3/24/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClparserServiceApplication.class)
@WebAppConfiguration
public class TestPricingService {

    private final PricingService pricingService = new PricingService();

    @Before
    public void init() {
    }

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
