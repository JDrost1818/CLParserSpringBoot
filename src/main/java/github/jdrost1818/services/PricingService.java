package github.jdrost1818.services;

import github.jdrost1818.model.Item;
import github.jdrost1818.repository.ItemRepository;
import github.jdrost1818.util.JSoupAddOn;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * This class is charged with all things relating to finding the most
 * accurate price for any given item. It is in charge of caching prices
 * and making the quickest decision possible while maintaining accurate
 * information based on the current street prices of items.
 *
 * Currently the only source of pricing data is:
 *
 * http://www.thepricegeek.com/
 *
 * However, further progress may be made to include other sources in addition.
 *
 * Created by JAD0911 on 3/24/2016.
 */
@Component
public class PricingService {

    @Autowired
    private ItemRepository itemRepository;

    public static final String BASE_URL = "http://www.thepricegeek.com/results/";
    public static final String NULL_URL = "http://www.thepricegeek.com/?no_results=";
    public static final String URL_SUFFIX = "?country=us";

    public static final BigDecimal NO_PRICING_DATA = BigDecimal.valueOf(0);

    /**
     * Utilizes the DB to access prices faster than it would be
     * to hit the pricing service via the web. Also checks that
     * the price we have is up to date and is viable to use, if
     * it isn't, it will look up the price and store it for use
     * later.
     *
     * @param item this for which to find the price
     * @return price of the given item
     */
    public BigDecimal calcPrice(Item item) {
        BigDecimal price = NO_PRICING_DATA;
        if (item != null && item.isStillValid()) {
            // Check if the item is cached first
            price = item.getPrice();
        } else if (item != null) {
            // Finds a newer price
            price = findNewPriceForString(item.getName());

            // Updates the model for caching
            Item newItem = new Item(item.getName());
            newItem.setDateCached(new Date());
            newItem.setPrice(price);

            // Persists the change
            itemRepository.save(newItem);
        }

        return price;
    }

    /**
     * Finds the total price of all items given while utilizing the cache
     *
     * @param items items for which to find the price
     * @return price of all the items
     */
    public BigDecimal calcPrice(List<Item> items) {
        BigDecimal price = NO_PRICING_DATA;
        if (items != null) {
            for (Item curItem : items) {
                price = price.add(calcPrice(curItem));
            }
        }

        return price;
    }

    /**
     * Is given a query and determines the price by calling
     * helper functions depending on if we have the item
     * in the database or not
     *
     * @param itemQuery the string representing the item we want to find the price
     * @return the market price of the item if we can find it
     */
    public BigDecimal calcPrice(String itemQuery) {
        BigDecimal price = NO_PRICING_DATA;

        // If this is an item we have in our DB, we should find the
        // price via the item itself so that we can utilize the cache
        if (itemQuery != null) {
            Item foundItem = itemRepository.findOne(itemQuery);
            if (foundItem == null) {
                price = findNewPriceForString(itemQuery);
            } else {
                price = calcPrice(foundItem);
            }
        }

        return price;
    }

    private BigDecimal findNewPriceForString(String itemQuery) {
        String url = queryToUrl(itemQuery);
        Document doc = JSoupAddOn.connect(url);

        return extractPrice(doc);
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
            // For http://www.thepricegeek.com/ the price is stored in a meta tag
            // EXAMPLE:
            //      <meta name="description" content="The price of anything is about $8.00, based on 51 historic results.">
            // If no results:
            //        <meta name="description" content="The Price Geek calculates the average price of items you see in marketplaces like eBay and Amazon, so you don't get ripped off.">
            String description = JSoupAddOn.getMetaTag(doc, "name", "description");
            if (description != null && description.contains("$")) {
                String priceString = description.split("\\$")[1].split(",")[0];
                if (priceString.matches("[-+]?\\d*\\.?\\d+")) {
                    price = new BigDecimal(priceString);
                }
            }
        }

        return price;
    }

    /**
     * Converts the item to a valid url for a service call
     *
     * @param query the string representing the item
     * @return a valid url to contact our service
     */
    String queryToUrl(String query) {
        if (query == null || "".equals(query)) {
            return NULL_URL;
        }

        return String.format("%s%s%s", BASE_URL, query.replace(" ", "+"), URL_SUFFIX);
    }
}
