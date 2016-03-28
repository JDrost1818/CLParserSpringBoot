package github.jdrost1818.services;

import github.jdrost1818.model.SearchCriteria;
import github.jdrost1818.model.craigslist.CraigslistPost;
import github.jdrost1818.repository.craigslist.CraigslistPostRepository;
import github.jdrost1818.util.JSoupAddOn;
import github.jdrost1818.util.StringUtil;
import org.apache.log4j.Logger;
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
import java.util.stream.Collectors;

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

    private static final Logger logger = Logger.getLogger(CraigslistService.class);

    /**
     * Searches for all posts that are matched by the {@link SearchCriteria}
     * and for which the user has not seen. Terminates when either:
     * 1 - when the last page is reached
     * 2 - when posts are found older than the earliestDate
     *
     * @param searchCriteria criteria for which to search
     * @param earliestDate   the farthest date back which we will accept a post as new
     * @return list of all posts that meet the criteria and have not been seen by the user
     */
    public List<CraigslistPost> search(SearchCriteria searchCriteria, Date earliestDate) {
        List<CraigslistPost> newPosts = new ArrayList<>();
        List<CraigslistPost> postsFromPage;
        Document doc;

        if (searchCriteria != null) {
            int curPage = 0;
            int numNewPostsFoundOnPage = NUM_RESULTS_PER_PAGE;
            String baseUrl = getBaseUrl(searchCriteria.getCity());

            // Iterate over whole pages
            while (numNewPostsFoundOnPage == NUM_RESULTS_PER_PAGE) {
                String curUrl = createCraigslistUrl(searchCriteria, ++curPage * NUM_RESULTS_PER_PAGE);
                doc = JSoupAddOn.connect(curUrl);

                postsFromPage = parsePage(earliestDate, doc, baseUrl);
                numNewPostsFoundOnPage = postsFromPage.size();
                newPosts.addAll(postsFromPage);
            }
        }

        return newPosts;
    }

    /**
     * Takes in a page and returns all posts that have been created
     * or updated since the date given
     *
     * @param earliestDate the farthest date back which we will accept a post as new
     * @param doc          the html element to parse
     * @param baseUrl      the domain of Craigslist - this changes based on city
     * @return all posts created since the date provided
     */
    public List<CraigslistPost> parsePage(Date earliestDate, Document doc, String baseUrl) {
        List<CraigslistPost> posts = new ArrayList<>();

        if (doc != null) {
            // Gets the container which has the actual posts
            Elements allPosts = doc.select(POSTS_TAG);

            // Adds all posts the user hasn't seen to the returned list
            allPosts.stream()
                    .forEach(html -> posts.add(loadPost(html, baseUrl)));
        }

        return posts.stream()
                .filter(post -> post != null && (earliestDate == null || post.getDateUpdated().after(earliestDate)))
                .collect(Collectors.toList());
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
            logger.error("Could not parse date - invalid post format");
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
        Integer minPrice = criteria.getMinPrice();
        Integer maxPrice = criteria.getMaxPrice();
        String match = StringUtil.format(criteria.getMatch(), "").replace(" ", "+");
        String exclu = StringUtil.format(criteria.getExclusions(), "").replace(" ", "+-");
        if (!"".equals(exclu))
            exclu = "+-" + exclu;

        return String.format("%ss=%s&query=%s%s&min_price=%s&max_price=%s&srchType=%s&hasPic=%s&postedToday=%s&searchNearby=%s",
                getBaseUrlSearchUrl(city, category),
                StringUtil.conditionalFormat(firstResultIndex > 0, Integer.toString(firstResultIndex), ""),
                match,
                exclu,
                StringUtil.format(minPrice, ""),
                StringUtil.format(maxPrice, ""),
                StringUtil.conditionalFormat(criteria.isSearchTitlesOnly(), "T", ""),
                StringUtil.conditionalFormat(criteria.isHasPic(), "1", ""),
                StringUtil.conditionalFormat(criteria.isPostedToday(), "1", ""),
                StringUtil.conditionalFormat(criteria.isIncludeNearbyAreas(), "1", ""));
    }

}
