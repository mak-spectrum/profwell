package org.profwell.vacancy.domain;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.profwell.common.model.Country;
import org.profwell.generic.domain.FormatsHolder;
import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.utils.NumberUtils;
import org.profwell.vacancy.model.Currency;
import org.profwell.vacancy.model.RequiredSkill;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyPriority;
import org.profwell.vacancy.model.VacancyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractVacancyForm extends ValidationForm {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractVacancyForm.class);

    protected long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    protected VacancyStatus status;
    protected Date openingDate;
    protected Date dueDate;
    protected String dueDateValue;
    protected Date closingDate;

    protected String companyName;
    protected String companyDetails;
    protected String companySocialBenefits;

    protected String positionCaption;
    protected String positionDetails;
    protected String positionProjectName;
    protected String positionProjectDetails;
    protected List<RequiredSkillForm> skills = new ArrayList<>();

    protected VacancyPriority priority = VacancyPriority.LOW;
    protected Country country;
    protected String city;
    protected Integer salaryFrom;
    protected String salaryFromValue;
    protected Integer salaryTill;
    protected String salaryTillValue;
    protected Currency salaryCurrency;

    public void transferFrom(Vacancy vacancy) {

        this.id = vacancy.getId();
        this.status = vacancy.getStatus();
        this.openingDate = vacancy.getOpeningDatetime();
        if (vacancy.getDueDate() != null) {
            this.dueDate = vacancy.getDueDate();
            this.dueDateValue = FormatsHolder.DATE_FORMATTER
                    .format(vacancy.getDueDate());
        }
        this.closingDate = vacancy.getClosingDatetime();

        this.companyName = vacancy.getCompany().getName();
        this.companyDetails = vacancy.getCompany().getDetails();
        this.companySocialBenefits = vacancy.getCompany().getSocialBenefits();

        this.positionCaption = vacancy.getPosition().getCaption();
        this.positionDetails = vacancy.getPosition().getDetails();
        this.positionProjectName = vacancy.getPosition().getProjectName();
        this.positionProjectDetails = vacancy.getPosition().getProjectDetails();

        this.skills.clear();
        for (RequiredSkill skill : vacancy.getPosition().getRequiredSkills()) {
            RequiredSkillForm skillForm = new RequiredSkillForm();
            skillForm.transferFrom(skill);
            this.skills.add(skillForm);
        }

        this.priority = vacancy.getPriority();
        if (vacancy.getSalaryRange() != null) {
            this.salaryFrom = vacancy.getSalaryRange().getFrom();
            this.salaryFromValue = String.valueOf(this.salaryFrom);
            this.salaryTill = vacancy.getSalaryRange().getTill();
            this.salaryTillValue = String.valueOf(this.salaryTill);
            this.salaryCurrency = vacancy.getSalaryRange().getCurrency();
        }

        this.country = vacancy.getCountry();
        this.city = vacancy.getCity();

    }

    public boolean isVacancyOpen() {
        return this.status == VacancyStatus.OPENED;
    }

    public boolean isVacancySuspended() {
        return this.status == VacancyStatus.SUSPENDED;
    }

    public boolean isVacancyActive() {
        return this.status == VacancyStatus.SUSPENDED
                || this.status == VacancyStatus.OPENED;
    }

    public boolean isVacancyClosed() {
        return this.status == VacancyStatus.CLOSED;
    }

    public String getOpeningDatetimeFormatted() {
        return FormatsHolder.TIME_FORMATTER.format(openingDate);
    }

    public String getOpeningDateFormatted() {
        return FormatsHolder.DATE_FORMATTER.format(openingDate);
    }

    public String getDueDateFormatted() {
        if (this.dueDate == null) {
            return "";
        } else {
            return FormatsHolder.DATE_FORMATTER.format(dueDate);
        }
    }

    public void setDueDateValue(String dueDateValue) {
        this.dueDateValue = dueDateValue;
        try {
            this.dueDate = DateUtils.parseDate(dueDateValue,
                    FormatsHolder.DATE_FORMAT);
        } catch (ParseException pe) {
            LOGGER.debug("Vacancy Due date parse exception.", pe);
        }
    }

    public String getClosingDatetimeFormatted() {
        return FormatsHolder.TIME_FORMATTER.format(closingDate);
    }

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String idvalue) {
        this.id = Long.parseLong(idvalue);
    }

    public String getSalaryFromValue() {
        return this.salaryFromValue;
    }

    public void setSalaryFromValue(String salaryFromValue) {
        this.salaryFromValue = salaryFromValue;
        this.salaryFrom = NumberUtils.parseInteger(salaryFromValue);
    }

    public String getSalaryTillValue() {
        return this.salaryTillValue;
    }

    public void setSalaryTillValue(String salaryTillValue) {
        this.salaryTillValue = salaryTillValue;
        this.salaryTill = NumberUtils.parseInteger(salaryTillValue);
    }

    public List<RequiredSkillForm> getMandatorySkills() {
        List<RequiredSkillForm> result = new ArrayList<>();
        for (RequiredSkillForm skillForm : this.skills) {
            if (skillForm.isMandatory()) {
                result.add(skillForm);
            }
        }
        Collections.sort(result);
        return result;
    }

    public List<RequiredSkillForm> getOptionalSkills() {
        List<RequiredSkillForm> result = new ArrayList<>();
        for (RequiredSkillForm skillForm : this.skills) {
            if (!skillForm.isMandatory()) {
                result.add(skillForm);
            }
        }
        Collections.sort(result);
        return result;
    }

    /* SIMPLE SETTERS/GETTERS */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VacancyStatus getStatus() {
        return status;
    }

    public void setStatus(VacancyStatus status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDetails() {
        return companyDetails;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public String getCompanySocialBenefits() {
        return companySocialBenefits;
    }

    public void setCompanySocialBenefits(String companySocialBenefits) {
        this.companySocialBenefits = companySocialBenefits;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPositionCaption() {
        return positionCaption;
    }

    public void setPositionCaption(String positionCaption) {
        this.positionCaption = positionCaption;
    }

    public String getPositionDetails() {
        return positionDetails;
    }

    public void setPositionDetails(String positionDetails) {
        this.positionDetails = positionDetails;
    }

    public String getPositionProjectName() {
        return positionProjectName;
    }

    public void setPositionProjectName(String positionProjectName) {
        this.positionProjectName = positionProjectName;
    }

    public String getPositionProjectDetails() {
        return positionProjectDetails;
    }

    public void setPositionProjectDetails(String positionProjectDetails) {
        this.positionProjectDetails = positionProjectDetails;
    }

    public Integer getSalaryFrom() {
        return salaryFrom;
    }

    public Integer getSalaryTill() {
        return salaryTill;
    }

    public List<RequiredSkillForm> getSkills() {
        return skills;
    }

    public Currency getSalaryCurrency() {
        return salaryCurrency;
    }

    public VacancyPriority getPriority() {
        return priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getDueDateValue() {
        return dueDateValue;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

}
