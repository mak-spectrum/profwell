package org.profwell.vacancy.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.profwell.generic.model.ModelConstants;

@Embeddable
@Access(AccessType.FIELD)
public class VacancyCompany {

    @Column(name="COMPANY_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String name;

    @Column(name="COMPANY_DETAILS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String details;

    @Column(name="COMPANY_SOCIAL_BENEFITS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String socialBenefits;

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
