package org.profwell.common.service;

import org.profwell.common.domain.CityForm;
import org.profwell.generic.validation.utils.ValidationUtility;

public class CityValidator {

    public boolean validate(CityForm form) {
        boolean result = true;

        result &= ValidationUtility.validateRequiredText(
                "name", form.getName(), form.getVc());

        result &= ValidationUtility.validateDropDown(
                        "countryValue", form.getCountryValue(), form.getVc());

        return result;
    }

}

