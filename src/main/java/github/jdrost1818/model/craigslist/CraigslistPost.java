package github.jdrost1818.model.craigslist;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static github.jdrost1818.data.CraigslistConstants.ID_TAG;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "CRAIGSLIST_POST")
public class CraigslistPost {

    private static final Logger logger = Logger.getLogger(CraigslistPost.class);

    @Id
    @Column(name = "id")
    private String id = "";

    private String title = "";
    @Column(length = 10000)
    private String description = "";
    private String link = "";
    private String county = "";
    private String category = "";
    private String location = "";

    private Integer price = -1;
    private Integer value = -1;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datePosted = new Date(0);
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated = new Date(0);
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCached = new Date(0);

    public static CraigslistPost parsePost(Element htmlElement, String baseUrl) {
        CraigslistPost newPost = new CraigslistPost();

        newPost.setId(htmlElement.attr(ID_TAG));
        if (StringUtils.isEmpty(newPost.getId())) {
            throw new IllegalArgumentException("Did not provide a post");
        }

        Elements priceElement = htmlElement.select("a span.price");
        if (!CollectionUtils.isEmpty(priceElement)) {
            newPost.setPriceByString(priceElement.text());
        }

        Element detailElement = htmlElement.select("span.txt").get(0);
        Element plElement = detailElement.select("span.pl").get(0);
        String dateString = plElement.select("time").attr("datetime");
        try {
            newPost.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(dateString));
        } catch (ParseException e) {
            logger.error("Malformed Date Found In Craigslist Post - (" + dateString + ")", e);
        }

        newPost.setLink(String.format("%s%s", baseUrl, plElement.select("a").attr("href")).replace("\\\\", "\\"));
        newPost.setTitle(plElement.select("a span#titletextonly").text());

        Element l2Element = detailElement.select("span.l2").get(0);
        String rawLocationText = l2Element.select("span.pnr").text(); // EXAMPLE - ' (Fridley) pic map'
        String actualLocation = rawLocationText.split("\\)")[0].trim().replace("(", "");
        newPost.setLocation(actualLocation);

        return newPost;
    }

    private void setPriceByString(String priceString) {
        if (!"".equals(priceString))
            setPrice(Integer.parseInt(priceString.replace("$", "")));
    }

    public boolean deepEquals(CraigslistPost otherPost) {
        return reflectionEquals(this, otherPost, "datePosted", "dateUpdated", "dateCached");
    }

    public void update(CraigslistPost post) {
        if (post != null && getId().equals(post.getId())) {
            setTitle(post.getTitle());
            setDescription(post.getDescription());
            setLink(post.getLink());
            setCounty(post.getCounty());
            setCategory(post.getCategory());
            setLocation(post.getLocation());
            setPrice(post.getPrice());
            setValue(post.getValue());
            setDatePosted(post.getDatePosted());
            setDateUpdated(post.getDateUpdated());
        }
    }
}
