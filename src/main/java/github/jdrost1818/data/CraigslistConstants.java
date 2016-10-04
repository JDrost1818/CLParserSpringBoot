package github.jdrost1818.data;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public final class CraigslistConstants {

    private static final String BASE_URL = "https://%s.craigslist.org/";
    private static final String BASE_URL_SEARCH_URL = "https://%s.craigslist.org/search/%s?";

    public static final Integer NUM_RESULTS_PER_PAGE = 100;

    public static final String POST_WRAPPER_TAG = "div.content";
    public static final String POST_TAG = "p.row";
    public static final String POSTS_TAG = POST_WRAPPER_TAG + " " + POST_TAG;
    public static final String ID_TAG = "data-pid";

    private CraigslistConstants() {
        // Prevent instantiation
    }

    public static String getBaseUrl(String city) {
        if (isEmpty(city)) {
            throw new IllegalArgumentException("Error: city argument must not be null or empty");
        }
        return String.format(BASE_URL, city);
    }

    public static String getBaseUrlSearchUrl(String city, String cat) {
        if (isEmpty(city)) {
            throw new IllegalArgumentException("Error: city argument must not be null or empty");
        }
        if (isEmpty(cat)) {
            return String.format(BASE_URL_SEARCH_URL, city, CraigslistCategory.ALL.all());
        }
        return String.format(BASE_URL_SEARCH_URL, city, cat);
    }
}
