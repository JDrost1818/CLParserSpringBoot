package github.jdrost1818.services;

import github.jdrost1818.data.CraigslistCategory;
import github.jdrost1818.data.CraigslistConstants;
import github.jdrost1818.model.SearchCriteria;
import github.jdrost1818.model.User;
import github.jdrost1818.model.craigslist.CraigslistPost;
import github.jdrost1818.repository.craigslist.CraigslistPostRepository;
import github.jdrost1818.util.JSoupAddOn;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static github.jdrost1818.data.CraigslistConstants.*;

/**
 * Created by JAD0911 on 3/24/2016.
 * <p>
 * This class is a simple interface for searching Craigslist and parsing search results.
 * It caches results in the database in order to limit hits on the Craigslist in order
 * to limit the number of hits on the Craigslist servers (prevents getting ip banned) and
 * to achieve performance gains.
 */
@Component
public class CraigslistService {

    @Autowired
    private CraigslistPostRepository craigslistPostRepository;

    /**
     * Searches for all posts that are matched by the {@link SearchCriteria}
     * and for which the user has not seen. Terminates when either:
     * 1 - when the search gets to posts already seen
     * 2 - when a post > 5 days old appears - this shouldn't really happen except first run
     * whichever comes first
     *
     * @param searchCriteria criteria for which to search
     * @param user           user that is making the search
     * @return list of all posts that meet the criteria and have not been seen by the user
     */
    public List<CraigslistPost> search(SearchCriteria searchCriteria, User user) {
        List<CraigslistPost> newPosts = new ArrayList<>();
        List<CraigslistPost> postsFromPage;
        Document doc;

        int curPage = 0;
        int numNewPostsFoundOnPage = NUM_RESULTS_PER_PAGE;
        String baseUrl = CraigslistConstants.getBaseUrl(searchCriteria.getCity());

        // Iterate over whole pages
        while (numNewPostsFoundOnPage == NUM_RESULTS_PER_PAGE) {
            String curUrl = createCraigslistUrl(searchCriteria, ++curPage * NUM_RESULTS_PER_PAGE);
            doc = JSoupAddOn.connect(curUrl);

            postsFromPage = parsePage(user, doc, baseUrl);
            numNewPostsFoundOnPage = postsFromPage.size();
            newPosts.addAll(postsFromPage);
        }
        return newPosts;
    }

    public List<CraigslistPost> parsePage(User user, Document doc, String baseUrl) {
        List<CraigslistPost> posts = new ArrayList<>();

        if (doc != null) {
            // Gets the container which has the actual posts
            Elements allPosts = doc.select(POST_WRAPPER_TAG).select(POST_TAG);

            // Adds all posts the user hasn't seen to the returned list
            allPosts.stream()
                    .filter(html -> !user.hasSeenPost(html.attr(ID_TAG)))
                    .forEach(html -> posts.add(loadPost(html, baseUrl)));
        }

        return posts;
    }

    /**
     * Returns a java representation of the html element.
     * It first looks to load the entry from the database
     * before parsing it as a new post.
     *
     * @param html    contains the html for a Craigslist post found on the site
     * @param baseUrl the home page for CL ie - https://minneapolis.craigslist.org
     * @return java representation of the Craigslist post
     */
    public CraigslistPost loadPost(Element html, String baseUrl) {
        String postId = html.attr(ID_TAG);
        CraigslistPost post = craigslistPostRepository.findOne(postId);
        Date dateUpdated;
        try {
            dateUpdated = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(html.select("time").attr("datetime"));
        } catch (ParseException e) {
            System.out.println("Could not parse date - invalid post format");
            return post;
        }

        // This means the post has not already been found
        // so we need to create it and save it in the db
        if (post == null) {
            post = new CraigslistPost(html, baseUrl);
        } else if (post.getDateCached().compareTo(dateUpdated) > 0) {
            post.update(new CraigslistPost(html, baseUrl));
        }

        post.setDateCached(new Date());
        craigslistPostRepository.save(post);

        return post;
    }

    /**
     * Transforms {@link SearchCriteria} into a valid URL to get a result page starting
     * from the the given index and on.
     *
     * @param criteria         criteria to use to make the url
     * @param firstResultIndex index of result to start from (deals with pagination)
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
        StringBuilder url = new StringBuilder(CraigslistConstants.getBaseUrlSearchUrl(city, category));
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
