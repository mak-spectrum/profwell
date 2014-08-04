package org.profwell.vacancy.service;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.utils.ValidationUtility;
import org.profwell.vacancy.domain.RequiredSkillForm;
import org.profwell.vacancy.domain.VacancyEditForm;

public class VacancyValidator {

    public boolean validate(VacancyEditForm form) {
        boolean result = true;

        ValidationContext context = form.getVc();

        if (!form.isNew()) {
            result &= validateDueDate(form, context);
        }

        result &= validatePosition(form, context);

        result &= validateCompany(form, context);

        result &= ValidationUtility.validateMaxLength(
                "city",
                form.getCity(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        if (form.isNew()) {
            result &= validateDueDate(form, context);
        }

        result &= validateSalary(form, context);

        return result;
    }

    private boolean validatePosition(VacancyEditForm form,
            ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateRequiredText(
                "positionCaption", form.getPositionCaption(), context);

        result &= ValidationUtility.validateMaxLength("positionDetails",
                form.getPositionDetails(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        result &= ValidationUtility.validateMaxLength("positionProjectName",
                form.getPositionDetails(),
                ModelConstants.STANDARD_TEXT_LIMIT,
                context);

        result &= ValidationUtility.validateMaxLength("positionProjectDetails",
                form.getPositionDetails(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        result &= this.validateSkillsNameLenght(form)
                & this.validateSkillsUniqueness(form);

        return result;
    }

    private boolean validateSkillsNameLenght(VacancyEditForm form) {
        boolean emptySkillExists = false;
        boolean overlongSkillExists = false;
        for (RequiredSkillForm skill : form.getSkills()) {
            if (StringUtils.isBlank(skill.getName())) {
                emptySkillExists = true;
                break;
            } else if (skill.getName().length() > ModelConstants.LONG_TEXT_LIMIT) {
                overlongSkillExists = true;
                break;
            }
        }

        if (emptySkillExists) {
            form.getVc().add("skills", "Skill name can't be empty.");
            return false;
        }
        if (overlongSkillExists) {
            form.getVc().add("skills", "Skill name can't be greater, than "
                    + ModelConstants.LONG_TEXT_LIMIT
                    + " symbols in length.");
            return false;
        }
        return true;
    }

    private boolean validateSkillsUniqueness(VacancyEditForm form) {
        boolean skillsUnique = true;
        for (RequiredSkillForm skill : form.getSkills()) {
            if (findEqualSkillForm(form, skill) != null) {
                skillsUnique = false;
                break;
            }
        }

        if (skillsUnique) {
            return true;
        } else {
            form.getVc().add("skills", "Skills names should be unique.");
            return false;
        }
    }

    private RequiredSkillForm findEqualSkillForm(VacancyEditForm vacancyForm,
            RequiredSkillForm skill) {
        for (RequiredSkillForm form : vacancyForm.getSkills()) {
            if (form.getName().toLowerCase()
                    .equals(skill.getName().toLowerCase())
                    && form != skill) {
                return form;
            }
        }

        return null;
    }

    private boolean validateCompany(VacancyEditForm form, ValidationContext context) {

        boolean result = true;

        result &= ValidationUtility.validateRequiredText("companyName",
                form.getCompanyName(),
                context);

        result &= ValidationUtility.validateMaxLength("companyDetails",
                form.getCompanyDetails(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        result &= ValidationUtility.validateMaxLength("companySocialBenefits",
                form.getCompanySocialBenefits(),
                ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT,
                context);

        return result;
    }

    private boolean validateDueDate(VacancyEditForm form, ValidationContext context) {

        boolean result = true;

        if (StringUtils.isNotBlank(form.getDueDateValue())) {

            if (form.getDueDate() == null) {
                context.add("dueDateValue",
                        "Please input date in format mm/dd/yyy (month/day/year).");
                result = false;
            }

        }

        return result;
    }

    private boolean validateSalary(VacancyEditForm form, ValidationContext context) {

        boolean result = true;

        if (StringUtils.isNotBlank(form.getSalaryFromValue())) {

            if (form.getSalaryFromValue().length() > 10) {
                context.add("salaryFromValue",
                        "Salary from field value can't be greater than 10 symbols in length.");
                result = false;
            } else if (form.getSalaryFrom() == null) {
                context.add("salaryFromValue",
                        "Salary from field value should consist of numbers only.");
                result = false;
            }

        }

        if (StringUtils.isNotBlank(form.getSalaryTillValue())) {

            if (form.getSalaryTillValue().length() > 10) {
                context.add("salaryTillValue",
                        "Salary till field value can't be greater than 10 symbols in length.");
                result = false;
            } else if (form.getSalaryTill() == null) {
                context.add("salaryTillValue",
                        "Salary till field value should consist of numbers only.");
                result = false;
            }

        }

        if (form.getSalaryFrom() != null && form.getSalaryTill() != null
                && form.getSalaryFrom() > form.getSalaryTill()) {
            context.add("salaryCurrencyValue",
                    "Salary range beginning can't be more than the range ending.");
            result = false;
        }

        if ((form.getSalaryFrom() != null || form.getSalaryTill() != null)
                && form.getSalaryCurrency() == null) {
            context.add("salaryCurrencyValue",
                    "Please select salary currency.");
            result = false;
        }

        return result;
    }

}
