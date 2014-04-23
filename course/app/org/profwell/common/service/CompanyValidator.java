package org.profwell.common.service;

import org.profwell.common.domain.CompanyForm;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.utils.ValidationUtility;

public class CompanyValidator {

    public boolean validate(CompanyForm form) {
        boolean result = true;

        ValidationContext context = form.getVc();

        result &= ValidationUtility.validateRequiredText("name",
                form.getName(), context);

        result &= ValidationUtility.validateMaxLength("details",
                form.getDetails(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        result &= ValidationUtility.validateMaxLength("socialBenefits",
                form.getSocialBenefits(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        return result;
    }

}

