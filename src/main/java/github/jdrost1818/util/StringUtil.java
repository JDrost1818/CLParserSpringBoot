package github.jdrost1818.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jake on 3/28/2016.
 */
public class StringUtil {

    private StringUtil() {
        // Prevent instantiation
    }

    public static String format(Object obj, String defaultString) {
        if (obj == null) {
            return defaultString;
        } else if (obj instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            return formatter.format((Date) obj);
        } else {
            return obj.toString();
        }
    }

    public static String conditionalFormat(boolean shouldReturn, String acceptString, String rejectString) {
        return (shouldReturn) ? acceptString : rejectString;
    }
}
