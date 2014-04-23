package org.profwell.common.service;

import org.profwell.common.domain.SkillForm;
import org.profwell.generic.validation.utils.ValidationUtility;

public class SkillValidator {

    public boolean validate(SkillForm form) {
        return ValidationUtility
                .validateRequiredText("name", form.getName(), form.getVc());
    }

}

