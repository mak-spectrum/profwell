package org.profwell.ui.select;

import java.util.ArrayList;
import java.util.List;

import org.profwell.generic.model.Dictionary;

public final class DictionaryConversionUtils {

    public static final String EMPTY_DROP_DOWN_VALUE = "NULL";

    private DictionaryConversionUtils() {
        // Nothing inside
    }

    public static List<SelectItem> convertDictionary(Dictionary[] values, String unselectedCaption) {

        List<SelectItem> result = new ArrayList<>();

        if (unselectedCaption != null) {
            result.add(new SelectItem(EMPTY_DROP_DOWN_VALUE, unselectedCaption));
        }

        for (Dictionary d : values) {
            result.add(new SelectItem(d.getName(), d.getCaption()));
        }

        return result;
    }

    public static List<SelectItem> convertDictionary(Dictionary[] values) {
        return convertDictionary(values, null);
    }

}
