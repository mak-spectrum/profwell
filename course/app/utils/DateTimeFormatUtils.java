package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateTimeFormatUtils {

    private DateTimeFormatUtils() {
        // Empty intentionally
    }

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "yyyy/MM/dd HH:mm", Locale.US);

    public static String getDatetimeFormatted(Date date) {
        return TIME_FORMAT.format(date);
    }
}
