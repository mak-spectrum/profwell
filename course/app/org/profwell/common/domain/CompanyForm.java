package org.profwell.common.domain;

import org.profwell.common.model.Company;
import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;

public class CompanyForm extends ValidationForm {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String name;
    private String details;
    private String socialBenefits;

    public void transferFrom(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.details = company.getDetails();
        this.socialBenefits = company.getSocialBenefits();
    }

    public void transferTo(Company company) {
        company.setName(this.name);
        company.setDetails(this.details);
        company.setSocialBenefits(this.socialBenefits);
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

    /* SIMPLE SETTERS/GETTERS */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSocialBenefits() {
        return socialBenefits;
    }

    public void setSocialBenefits(String socialBenefits) {
        this.socialBenefits = socialBenefits;
    }

}
