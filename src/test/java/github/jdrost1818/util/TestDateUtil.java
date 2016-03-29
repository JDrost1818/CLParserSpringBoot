package github.jdrost1818.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testGetXDaysAgo() {
        Date today = new Date();
        Date shouldBeYesterday = DateUtil.getXDaysAgo(1);

        assertTrue(shouldBeYesterday.before(today));

        String todayDayString = DateUtil.formatDate(today).split("/")[1];
        String yesterdayDayString = DateUtil.formatDate(shouldBeYesterday).split("/")[1];

        int todayNum = Integer.parseInt(todayDayString);
        int yesterdayNum = Integer.parseInt(yesterdayDayString);

        if (todayNum > yesterdayNum) {
            assertEquals(todayNum, yesterdayNum + 1);
        } else {
            // Just in case we test on the first of a month
            assertTrue(todayNum < yesterdayNum);
        }
    }

}
