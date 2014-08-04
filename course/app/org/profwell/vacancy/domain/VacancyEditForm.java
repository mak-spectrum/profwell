package org.profwell.vacancy.domain;

import java.util.Iterator;
import java.util.List;

import org.profwell.common.model.Country;
import org.profwell.ui.select.DictionaryConversionUtils;
import org.profwell.ui.select.SelectItem;
import org.profwell.vacancy.model.Currency;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.SalaryRange;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyCompany;
import org.profwell.vacancy.model.VacancyPosition;
import org.profwell.vacancy.model.VacancyPriority;

public class VacancyEditForm extends AbstractVacancyForm {

    public void transferTo(Vacancy vacancy) {

        vacancy.setPriority(this.priority);
        vacancy.setDueDate(this.dueDate);

        if (vacancy.getCompany() == null) {
            vacancy.setCompany(new VacancyCompany());
        }
        vacancy.getCompany().setName(this.companyName);
        vacancy.getCompany().setDetails(this.companyDetails);
        vacancy.getCompany().setSocialBenefits(this.companySocialBenefits);

        if (vacancy.getPosition() == null) {
            vacancy.setPosition(new VacancyPosition());
        }
        vacancy.getPosition().setCaption(positionCaption);
        vacancy.getPosition().setDetails(this.positionDetails);
        vacancy.getPosition().setProjectName(this.positionProjectName);
        vacancy.getPosition().setProjectDetails(this.positionProjectDetails);

        this.removeLostSkills(vacancy);
        this.transferActualSkills(vacancy);

        if (vacancy.getSalaryRange() == null) {
            vacancy.setSalaryRange(new SalaryRange());
        }
        vacancy.getSalaryRange().setFrom(this.salaryFrom);
        vacancy.getSalaryRange().setTill(this.salaryTill);
        vacancy.getSalaryRange().setCurrency(salaryCurrency);
        vacancy.setCountry(this.country);
        vacancy.setCity(this.city);
    }

    private void removeLostSkills(Vacancy vacancy) {
        Iterator<RequiredSkill> it = vacancy.getPosition()
                .getRequiredSkills().iterator();
        while (it.hasNext()) {
            if (findSkillForm(it.next().getName()) == null) {
                it.remove();
            }
        }
    }

    private RequiredSkillForm findSkillForm(String name) {
        for (RequiredSkillForm form : this.skills) {
            if (form.getName().equals(name)) {
                return form;
            }
        }

        return null;
    }

    private void transferActualSkills(Vacancy vacancy) {
        for (RequiredSkillForm form : skills) {
            RequiredSkill skill = this.findPersonSkill(vacancy, form.getName());
            if (skill == null) {
                skill = new RequiredSkill();
                form.transferTo(skill);
                skill.setVacancy(vacancy);
                vacancy.getPosition().getRequiredSkills().add(skill);
            } else {
                form.transferTo(skill);
            }
        }
    }

    private RequiredSkill findPersonSkill(Vacancy vacancy, String name) {
        for (RequiredSkill skill : vacancy.getPosition().getRequiredSkills()) {
            if (name.equals(skill.getName())) {
                return skill;
            }
        }
        return null;
    }

    public boolean isGeneralSectionExpanded() {
        return true;
    }

    public boolean isPositionInfoSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "positionCaption",
                    "positionDetails",
                    "positionProjectName",
                    "positionProjectDetails",
                    "positionRequiredSkills");
        }
    }

    public boolean isCompanyInfoSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "companyName", "companyDetails", "companySocialBenefits");
        }
    }

    public boolean isOtherInfoSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "countryValue",
                    "city",
                    "salaryFrom",
                    "salaryTill",
                    "salaryCurrencyValue");
        }
    }


    public String getSalaryCurrencyValue() {
        if (this.salaryCurrency == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return this.salaryCurrency.getName();
        }
    }

    public void setSalaryCurrencyValue(String currencyValue) {
        for (Currency c : Currency.values()) {
            if (c.getName().equals(currencyValue)) {
                this.salaryCurrency = c;
                return;
            }
        }
        this.salaryCurrency = null;
    }

    public List<SelectItem> getCurrencySelectItems() {
        return DictionaryConversionUtils.convertDictionary(
                Currency.getValues(),
                "-");
    }

    public String getCountryValue() {
        if (this.country == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return this.country.getName();
        }
    }

    public void setCountryValue(String countryValue) {
        for (Country c : Country.values()) {
            if (c.getName().equals(countryValue)) {
                this.country = c;
                return;
            }
        }
        this.country = null;
    }

    public List<SelectItem> getCountrySelectItems() {
        return DictionaryConversionUtils.convertDictionary(
                Country.getValues(),
                "-select country-");
    }

    public String getPriorityValue() {
        if (this.priority == null) {
            return VacancyPriority.LOW.getName();
        } else {
            return this.priority.getName();
        }
    }

    public void setPriorityValue(String priorityValue) {
        for (VacancyPriority vp : VacancyPriority.values()) {
            if (vp.getName().equals(priorityValue)) {
                this.priority = vp;
                return;
            }
        }
        this.priority = VacancyPriority.LOW;
    }

    public List<SelectItem> getPrioritySelectItems() {
        return DictionaryConversionUtils.convertDictionary(
                VacancyPriority.getValues());
    }
}
