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
    public static final String ID_TAG = "data-pid";

    public static String getBaseUrl(String city) {
        return String.format(BASE_URL, city);
    }

    public static String getBaseUrlSearchUrl(String city, String cat) {
        return String.format(BASE_URL_SEARCH_URL, city, cat);
    }
}
