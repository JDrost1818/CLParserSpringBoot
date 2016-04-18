package github.jdrost1818.util;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Contains tools to make interfacing with the Jsoup API
 * easier when using it in this project
 *
 * Created by JAD0911 on 3/24/2016.
 */
public final class JSoupAddOn {

    private static final Logger logger = Logger.getLogger(JSoupAddOn.class);

    private JSoupAddOn() {
        // Prevent instantiation
    }

    /**
     * Connects to a website using JSoup. This makes it easier to connect
     * in code rather than having to deal with the try catch. It also
     * retries to connect before giving up. This ensures the URL is malformed
     * or the site is down, not just a fluke - which has happened with this library.
     *
     * @param url connection string to website to connect to
     * @return the document of the website, or null if cannot connect
     */
    public static Document connect(String url) {
        Document doc = null;
        int numTries = 0;
        while (numTries < 5 && doc == null) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                logger.error("Error while trying to connect to URL. Retrying", e);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                    logger.error("Could not sleep", ignore);
                }
                numTries++;
            }
        }

        return doc;
    }

    // Found on StackOverflow @
    // http://stackoverflow.com/questions/9958425/get-title-meta-description-content-using-url
    public static String getMetaTag(Document document, String metaKey, String attr) {
        Elements elements = document.select("meta[" + metaKey + "=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null && !"".equals(s))
                return s;
        }

        return null;
    }
}
