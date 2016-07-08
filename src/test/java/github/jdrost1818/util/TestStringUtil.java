package github.jdrost1818.util;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jake on 3/28/2016.
 */
public class TestStringUtil {

    @Test
    public void testFormatString() {
        Object someObject = new Object();
        assertEquals(someObject.toString(), StringUtil.format(someObject, "Hello"));
    }

    @Test
    public void testFormatStringNull() {
        String defaultString = "";
        assertEquals(defaultString, StringUtil.format(null, defaultString));
    }

    @Test
    public void testFormatStringDate() {
        Date date = new Date(0);
        assertEquals("01/01/1970", StringUtil.format(date, ""));
    }

    @Test
    public void testConditionalFormatTrue() {
        String truthString = "da truth";
        assertEquals(truthString, StringUtil.conditionalFormat(true, truthString, ""));
    }

    @Test
    public void testConditionalFormatFalse() {
        String truthString = "da not truth";
        assertEquals(truthString, StringUtil.conditionalFormat(false, "", truthString));
    }
}
