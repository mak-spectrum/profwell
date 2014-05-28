package utils;

import java.util.Date;

public final class DashboardDateUtils {

    public static final int NOW = 0;

    public static final int TWO_WEEKS = 2*7;

    public static final int FOUR_WEEKS = 4*7;

    public static final int EIGHT_WEEKS = 8*7;

    private static final int A_DAY = 24*60*60*1000;

    private DashboardDateUtils() {}

    public static boolean isLessThan(long timerange, Date fromDate, Date toDate) {
        return (toDate.getTime() - fromDate.getTime())/A_DAY < timerange;
    }

}
