package github.jdrost1818.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by JAD0911 on 3/29/2016.
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
