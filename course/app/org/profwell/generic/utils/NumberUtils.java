package org.profwell.generic.utils;

import java.text.NumberFormat;
import java.text.ParseException;

public final class NumberUtils {

    private NumberUtils() {
        // NOTE: empty intentionally
    }

    public static Integer parseInteger(String string) {
        try {
            return NumberFormat.getIntegerInstance().parse(string).intValue();
        } catch (ParseException pe) {
            return null;
        }
    }

}
