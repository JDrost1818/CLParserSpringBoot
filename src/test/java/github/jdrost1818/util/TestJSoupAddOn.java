package github.jdrost1818.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by JAD0911 on 3/24/2016.
 */
public class TestJSoupAddOn {

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
}
