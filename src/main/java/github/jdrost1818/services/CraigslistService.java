package github.jdrost1818.services;

import github.jdrost1818.data.CraigslistCategory;
import github.jdrost1818.model.User;
import github.jdrost1818.model.craigslist.CraigslistPost;
import github.jdrost1818.model.SearchCriteria;
import github.jdrost1818.repository.craigslist.CraigslistPostRepository;
import github.jdrost1818.repository.UserRepository;
import github.jdrost1818.util.JSoupAddOn;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAD0911 on 3/24/2016.
 *
 * This class is a simple interface for searching Craigslist and parsing search results.
 * It caches results in the database in order to limit hits on the Craigslist in order
 * to limit the number of hits on the Craigslist servers (prevents getting ip banned) and
 * to achieve performance gains.
 */
@Component
public class CraigslistService {

    @Autowired
    private CraigslistPostRepository craigslistPostRepository;

    @Autowired
    private UserRepository userRepository;

    // This may eventually be customizable by user
    public static final String BASE_URL = "https://%s.craigslist.org/search/%s?";

    public static final Integer NUM_RESULTS_PER_PAGE = 100;

    /**
     * Searches for all posts that are matched by the {@link SearchCriteria}
     * and for which the user has not seen. Terminates when either:
     *      1 - when the search gets to posts already seen
     *      2 - when a post > 5 days old appears - this shouldn't really happen except first run
     * whichever comes first
     *
     * @param searchCriteria criteria for which to search
     * @param user           user that is making the search
     * @return list of all posts that meet the criteria and have not been seen by the user
     */
    public List<CraigslistPost> search(SearchCriteria searchCriteria, User user) {
        double curTime = System.currentTimeMillis();

        List<CraigslistPost> newPosts = new ArrayList<>();
        Document doc;

        int curPage = 0;
        int numEntries = 100;
        int numAlreadyVisited = 0;

        // Iterate over whole pages
        // Gonna limit to 3 pages for right now
        while (numEntries == 100 && numAlreadyVisited < 25 && curPage < 1) {
            String curUrl = createCraigslistUrl(searchCriteria, curPage*NUM_RESULTS_PER_PAGE);

            System.out.println("Scanning Page " + (++curPage) + " @ " + curUrl);
            doc = JSoupAddOn.connect(curUrl);

            if (doc == null) {
                System.out.println("Could not connect, moving on");
                break;
            } else {
                // Gets the number of posts found and isolates
                // the posts by trimming the document to only them
                numEntries = doc.select("p.row").size();
                Elements allPosts = doc.select("div.content");
                Element curPost;

                // Iterates through all the posts found for the page
                for (int j = 0; j < numEntries; j++) {
                    curPost = allPosts.select("p.row").get(j);
                    String curPostId = curPost.attr("data-pid");
                    System.out.println("Found post with id: " + curPostId);
                }
            }

        }
        System.out.println("Process took " + (System.currentTimeMillis() - curTime) / 1000 + " seconds");
        return newPosts;
    }

    /**
     * Transforms {@link SearchCriteria} into a valid URL to get a result page starting
     * from the the given index and on.
     *
     * @param criteria          Criteria to use to make the url
     * @param firstResultIndex  index of result to start from (deals with pagination)
     * @return a valid Craigslist url which links to results specified by the criteria
     */
    public String createCraigslistUrl(SearchCriteria criteria, int firstResultIndex) {
        String city = criteria.getCity();
        String category = criteria.getCategory();
        if (city == null || "".equals(city)) {
            throw new IllegalArgumentException("Error: city argument must not be null or empty");
        }
        if (criteria.getCategory() == null || "".equals(category)) {
            category = CraigslistCategory.ALL.all();
        }

        // Now that we know we can search, get the rest of the criteria
        Integer minPrice = criteria.getMinPrice();
        Integer maxPrice = criteria.getMaxPrice();
        String match = (criteria.getMatch() != null) ? criteria.getMatch() : "";
        String exclu = (criteria.getExclusions() != null) ? criteria.getExclusions() : "";

        // Creates the query aspect of the url
        StringBuilder url = new StringBuilder(String.format(BASE_URL, city, category));
        if (firstResultIndex > 0)
            url.append("s=").append(firstResultIndex);
        url.append("&query=");
        url.append(match.replace(" ", "+"));
        url.append(exclu.replace(" ", "+-"));

        // Add the price restrictions if they exist
        if (minPrice != null) {
            url.append("&minAsk=").append(minPrice);
        }
        if (maxPrice != null) {
            url.append("&maxAsk=").append(maxPrice);
        }

        // Add the boolean flags
        if (criteria.isSearchTitlesOnly())
            url.append("&srchType=T");
        if (criteria.isHasPic())
            url.append("&hasPic=1");
        if (criteria.isPostedToday())
            url.append("&postedToday=1");
        if (criteria.isIncludeNearbyAreas())
            url.append("&searchNearby=1");

        return url.toString();
    }

}
