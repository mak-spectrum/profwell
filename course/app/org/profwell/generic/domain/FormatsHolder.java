package org.profwell.generic.domain;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class FormatsHolder {

    public static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private FormatsHolder() {
    }

}
