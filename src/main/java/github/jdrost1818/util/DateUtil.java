package github.jdrost1818.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by JAD0911 on 3/29/2016.
 */
public final class DateUtil {

    public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    private DateUtil() {
        // Prevent instantiation
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "00/00/0000";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static Date getXDaysAgo(int daysAgo) {
        return new Date(System.currentTimeMillis() - (daysAgo * DAY_IN_MS));
    }
}
