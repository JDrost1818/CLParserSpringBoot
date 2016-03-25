package github.jdrost1818.services;

import github.jdrost1818.model.Item;
import github.jdrost1818.repository.ItemRepository;
import github.jdrost1818.util.JSoupAddOn;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JAD0911 on 3/24/2016.
 *
 * This class is charged with all things relating to finding the most
 * accurate price for any given item. It is in charge of caching prices
 * and making the quickest decision possible while maintaining accurate
 * information based on the current street prices of items.
 *
 * Currently the only source of pricing data is:
 *
 *  https://thepricegeek.com
 *
 * However, further progress may be made to include other sources in addition.
 */
@Component
public class PricingService {

    @Autowired
    private ItemRepository itemRepository;

    public static final String BASE_URL = "http://www.thepricegeek.com/results/";
    public static final String NULL_URL = "http://www.thepricegeek.com/?no_results=";
    public static final String URL_SUFFIX = "?country=us";

    public static final BigDecimal NO_PRICING_DATA = new BigDecimal(-1);


    /**
     * Public entry point to get the price of an item
     *
     * @param itemQuery string representing the item
     * @return the price of the item if we can find it
     */
    public BigDecimal getPrice(String itemQuery) {
        return parsePrice(itemQuery);
    }

    /**
     * Converts the item to a valid url for a service call
     *
     * @param query the string representing the item
     * @return a valid url to contact our service
     */
    public String queryToUrl(String query) {
        if (query == null || "".equals(query)) {
            return NULL_URL;
        }
        return String.format("%s%s%s", BASE_URL, query.replace(" ", "+"), URL_SUFFIX);
    }

    /**
     * Is given a query and determines the price by either:
     *      1 - checking the cache for the item
     *      2 - making a service call for item
     *
     * @param itemQuery the string representing the item we want to find the price
     * @return the market price of the item if we can find it
     */
    private BigDecimal parsePrice(String itemQuery) {
        BigDecimal price;

        // Repositories don't like null values for ids
        if (itemQuery == null) {
            return NO_PRICING_DATA;
        }

        Item foundItem = itemRepository.findOne(itemQuery);
        if (foundItem != null && foundItem.isStillValid()) {
            // Check if the item is cached first
            price = foundItem.getPrice();
        } else {
            // If not cached, or the cache is out
            // of date, determine the price
            String url = queryToUrl(itemQuery);
            Document doc = JSoupAddOn.connect(url);
            price = extractPrice(doc);

            // Updates the model for caching - creates
            // if this is an entirely new entry
            if (!price.equals(NO_PRICING_DATA)) {
                if (foundItem == null) {
                    foundItem = new Item(itemQuery);
                }
                foundItem.setDateCached(new Date());
                foundItem.setPrice(price);

                // Persists the change
                itemRepository.save(foundItem);
            }
        }
        return price;
    }

    /**
     * This encapsulates the logic needed to find the price
     * from the service from where we get the information
     *
     * @param doc the DOM for the service we contacted
     * @return the price of the service call if it exists
     */
    private BigDecimal extractPrice(Document doc) {
        BigDecimal price = NO_PRICING_DATA;

        if (doc != null) {
            String description = JSoupAddOn.getMetaTag(doc, "description");
            if (description != null && description.contains("$")) {
                String priceString = description.split("\\$")[1].split(",")[0];
                if (priceString.matches("[-+]?\\d*\\.?\\d+")) {
                    price = new BigDecimal(priceString);
                }
            }
        }

        return price;
    }
}
