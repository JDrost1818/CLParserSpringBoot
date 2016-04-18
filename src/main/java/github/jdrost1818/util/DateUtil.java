package github.jdrost1818.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Util class that is responsible for all things relating to dates in
 * the app. Essentially, if as a programmer, you need to manipulate/get
 * a date, that action should occur in this class
 *
 * Created by JAD0911 on 3/29/2016.
 */
public final class DateUtil {

    public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    private DateUtil() {
        // Prevent instantiation
    }

    /**
     * Ensures this app has a constant date format
     *
     * @param date date to format to a String
     * @return formatted date String
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "00/00/0000";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }

    /**
     * Simplifies getting past days
     *
     * @param daysAgo number of days to go back in time from the present
     * @return the date from the specified number of days in the past
     */
    public static Date getXDaysAgo(int daysAgo) {
        return new Date(System.currentTimeMillis() - (daysAgo * DAY_IN_MS));
    }
}
