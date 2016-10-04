package github.jdrost1818.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestDateUtil {

    @Test
    public void testFormatDate() {
        assertEquals("01/01/1970", DateUtil.formatDate(new Date(0)));
    }

    @Test
    public void testFormatDateNull() {
        assertEquals("00/00/0000", DateUtil.formatDate(null));
    }

}
