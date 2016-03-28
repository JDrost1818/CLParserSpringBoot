package github.jdrost1818.data;

/**
 * Created by Jake on 3/26/2016.
 */
public class CraigslistConstants {

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
        if (city == null || "".equals(city)) {
            throw new IllegalArgumentException("Error: city argument must not be null or empty");
        }
        return String.format(BASE_URL, city);
    }

    public static String getBaseUrlSearchUrl(String city, String cat) {
        if (city == null || "".equals(city)) {
            throw new IllegalArgumentException("Error: city argument must not be null or empty");
        }
        if (cat == null || "".equals(cat)) {
            return String.format(BASE_URL_SEARCH_URL, city, CraigslistCategory.ALL.all());
        }
        return String.format(BASE_URL_SEARCH_URL, city, cat);
    }
}
