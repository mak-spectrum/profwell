package org.profwell.common.service;

import org.profwell.common.domain.PositionForm;
import org.profwell.generic.validation.utils.ValidationUtility;

public class PositionValidator {

    public boolean validate(PositionForm form) {
        return ValidationUtility
                .validateRequiredText("name", form.getName(), form.getVc());
    }

}

