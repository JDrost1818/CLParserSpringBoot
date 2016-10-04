package github.jdrost1818.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Objects.isNull;

/**
 * Util class that is responsible for all things relating to dates in
 * the app. Essentially, if as a programmer, you need to manipulate/get
 * a date, that action should occur in this class
 *
 * @author Jake Drost
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DateUtil {

    /**
     * Ensures this app has a constant date format
     *
     * @param date date to format to a String
     * @return formatted date String
     */
    static String formatDate(Date date) {
        if (isNull(date)) {
            return "00/00/0000";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
