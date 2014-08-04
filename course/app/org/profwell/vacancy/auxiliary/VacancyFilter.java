package org.profwell.vacancy.auxiliary;

import org.profwell.common.model.Country;
import org.profwell.generic.auxiliary.GenericFilter;
import org.profwell.ui.select.DictionaryConversionUtils;
import org.profwell.vacancy.model.VacancyStatus;

public class VacancyFilter extends GenericFilter {

    private String company;

    private String project;

    private String position;

    private VacancyStatus status;

    private Long assigneeId;

    private Country country;

    private String city;

    private Long workspaceId;

    public String getStatusValue() {
        if (this.status == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return String.valueOf(this.status);
        }
    }

    public void setStatusValue(String status) {
        for (VacancyStatus vs : VacancyStatus.values()) {
            if (vs.getName().equals(status)) {
                this.status = vs;
                return;
            }
        }
        this.status = null;
    }

    public String getCountryValue() {
        if (this.country == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return String.valueOf(this.country);
        }
    }

    public void setCountryValue(String country) {
        for (Country c : Country.values()) {
            if (c.getName().equals(country)) {
                this.country = c;
                return;
            }
        }
        this.country = null;
    }

    public String getAssigneeIdValue() {
        return String.valueOf(this.assigneeId);
    }

    public void setAssigneeIdValue(String idValue) {
        this.assigneeId = Long.parseLong(idValue);
    }

    public VacancyStatus getStatus() {
        return this.status;
    }

    public void setStatus(VacancyStatus status) {
        this.status = status;
    }

    public Long getAssigneeId() {
        return this.assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

}
