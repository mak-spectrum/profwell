package org.profwell.generic.validation.utils;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.ValidationMessageBuilder;
import org.profwell.ui.select.DictionaryConversionUtils;

public final class ValidationUtility {

    private ValidationUtility() {
        // NOTE : Nothing inside
    }

    public static boolean validateNotEmpty(String fieldCode, String fieldValue,
            ValidationContext context) {

        if (StringUtils.isBlank(fieldValue)) {
            context.add(new ValidationMessageBuilder()
                    .source(fieldCode)
                    .message("Please fill in the field.")
                    .build());
            return false;
        }

        return true;
    }

    public static boolean validateMaxLength(String fieldCode, String fieldValue, int limit,
            ValidationContext context) {

        if (fieldValue != null && fieldValue.trim().length() > limit) {
            context.add(new ValidationMessageBuilder()
                    .source(fieldCode)
                    .message("Field can't be greater, than " + limit + " symbols in length.")
                    .build());
            return false;
        }

        return true;
    }

    public static boolean validateRequiredText(String fieldCode, String fieldValue,
            ValidationContext context) {

        if (validateNotEmpty(fieldCode, fieldValue, context)) {
            return validateMaxLength(fieldCode, fieldValue, ModelConstants.STANDARD_TEXT_LIMIT, context);
        } else {
            return false;
        }

    }

    public static boolean validateDropDown(String fieldCode, String fieldValue,
            ValidationContext context) {

        if (DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE.equals(fieldValue)) {
            context.add(new ValidationMessageBuilder()
                    .source(fieldCode)
                    .message("Please select a value.")
                    .build());
            return false;
        }

        return true;
    }

}
