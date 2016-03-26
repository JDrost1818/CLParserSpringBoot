package github.jdrost1818.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public interface JSoupAddOn {

    static Document connect(String url) {
        Document doc = null;
        int numTries = 0;
        while (numTries < 5 && doc == null) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                System.out.println("Error while trying to connect to URL. Retrying");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }
                numTries++;
            }
        }
        return doc;
    }

    // Found on StackOverflow @
    // http://stackoverflow.com/questions/9958425/get-title-meta-description-content-using-url
    static String getMetaTag(Document document, String metaKey, String attr) {
        Elements elements = document.select("meta[" + metaKey + "=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null && !"".equals(s))
                return s;
        }
        return null;
    }
}
