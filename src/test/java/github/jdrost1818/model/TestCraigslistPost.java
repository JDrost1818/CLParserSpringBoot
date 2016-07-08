package github.jdrost1818.model;

import static github.jdrost1818.data.CraigslistConstants.ID_TAG;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import github.jdrost1818.data.CraigslistConstants;
import github.jdrost1818.model.craigslist.CraigslistPost;

/**
 * Created by Jake on 3/26/2016.
 */
public class TestCraigslistPost {

    private static final String BASE_URL = CraigslistConstants.getBaseUrl("minneapolis");
    private static final String CWD = System.getProperty("user.dir");

    @Test
    public void testConstructor() throws ParseException, IOException {
        // These fields are found in the file /resources/html/JSoup.html
        String id = "5467151781";
        Integer price = 175;
        String link = BASE_URL + "/ank/tad/5467151781.html";
        String title = "Large Replica Sailboat";
        String location = "Fridley";
        Date updatedDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2016-03-26 11:08");

        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8");
        Element postHtml = doc.select(CraigslistConstants.POST_WRAPPER_TAG).select(CraigslistConstants.POST_TAG).get(0);
        CraigslistPost post = CraigslistPost.parsePost(postHtml, BASE_URL);

        assertEquals(id, post.getId());
        assertEquals(price, post.getPrice());
        assertEquals(link, post.getLink());
        assertEquals(title, post.getTitle());
        assertEquals(location, post.getLocation());
        assertEquals(updatedDate, post.getDateUpdated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderIdNull() {
        Element htmlElement = Mockito.mock(Element.class);
        Mockito.when(htmlElement.attr(ID_TAG)).thenReturn(null);

        CraigslistPost.parsePost(htmlElement, "");
    }

    @Test
    public void testBuilderWrongDateFormat() {
        Element htmlElement = Mockito.mock(Element.class);
        Mockito.when(htmlElement.attr(ID_TAG)).thenReturn("id");
        Elements elements = Jsoup.parse("" +
                "<span class=\"txt\"> " +
                "  <span class=\"pl\">" +
                "    <span class=\"l2\"> " +
                "      <span class=\"price\">$175</span> " +
                "        <span class=\"pnr\"> <small> (Fridley)</small>").children();
        Mockito.when(htmlElement.select("span.txt")).thenReturn(elements);

        CraigslistPost post = CraigslistPost.parsePost(htmlElement, "");
        assertEquals(post.getDatePosted(), new Date(0));
    }

    @Test
    public void tesUpdate() throws IOException {
        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8");
        Element postHtml = doc.select(CraigslistConstants.POST_WRAPPER_TAG).select(CraigslistConstants.POST_TAG).get(0);
        CraigslistPost post = CraigslistPost.parsePost(postHtml, BASE_URL);

        CraigslistPost postToBeUpdated = new CraigslistPost();
        postToBeUpdated.setId(post.getId());
        postToBeUpdated.update(post);

        assertTrue(postToBeUpdated.deepEquals(post));
    }

    @Test
    public void testDoesNotUpdateIfIdsNotSame() throws IOException {
        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8");
        Element postHtml = doc.select(CraigslistConstants.POST_WRAPPER_TAG).select(CraigslistConstants.POST_TAG).get(0);
        CraigslistPost post = CraigslistPost.parsePost(postHtml, BASE_URL);

        CraigslistPost postToBeUpdated = new CraigslistPost();
        postToBeUpdated.update(post);

        assertFalse(postToBeUpdated.deepEquals(post));
    }
}
