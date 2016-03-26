package github.jdrost1818.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public class TestJSoupAddOn {

    private String CWD = System.getProperty("user.dir");

    @Test
    public void testConnect() {
        // This could technically fail without the fault being
        // the functions fault - if we don't have access to the
        // internet, but this is a conscious decision to ignore that
        String url = "https://www.google.com";
        assertNotEquals(null, JSoupAddOn.connect(url));
    }

    @Test
    public void testConnectNotAWebsite() {
        String url = "https://www.ThisIsNotAWebsiteOrIfItIsTheyHaveATerribleURLAndPropsToThem.com";
        assertEquals(null, JSoupAddOn.connect(url));
    }

    @Test
    public void testGetMetaTagName() throws IOException {
        // Defined in the meta tag of the file /resources/html/JSoup.html
        String expected = "This is a description";

        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8", "www.example.com");
        String actual = JSoupAddOn.getMetaTag(doc, "name", "description");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMetaTagProperty() throws IOException {
        // Defined in the meta tag of the file /resources/html/JSoup.html
        String expected = "image.jpg";

        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8", "www.example.com");
        String actual = JSoupAddOn.getMetaTag(doc, "property", "image");

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMetaTagNoMatch() throws IOException {
        Document doc = Jsoup.parse(new File(CWD + "/src/test/java/resources/html/JSoup.html"), "UTF-8", "www.example.com");
        assertNull(JSoupAddOn.getMetaTag(doc, "property", "lsadkfsldkf"));
    }
}
