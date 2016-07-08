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
}
