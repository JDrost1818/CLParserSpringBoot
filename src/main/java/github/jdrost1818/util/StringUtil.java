package github.jdrost1818.util;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Jake on 3/28/2016.
 */
public final class StringUtil {

    private StringUtil() {
        // Prevent instantiation
    }

    public static String format(Object obj, String defaultString) {
        if (Objects.isNull(obj)) {
            return defaultString;
        } else if (obj instanceof Date) {
            return DateUtil.formatDate((Date) obj);
        } else {
            return obj.toString();
        }
    }

    public static String conditionalFormat(boolean shouldReturn, String acceptString, String rejectString) {
        return shouldReturn ? acceptString : rejectString;
    }
}
