package org.profwell.person.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.profwell.generic.model.ModelConstants;

@Embeddable
@Access(AccessType.FIELD)
public class Profession {

    @Enumerated(EnumType.STRING)
    @Column(name="PROFESSION_GENERAL_TYPE", nullable = false, length = 20)
    private ProfessionGeneralType generalType;

    @Column(name="PROFESSION_DETAILS", nullable = true,
            columnDefinition="TEXT",
            length = ModelConstants.STANDARD_TEXT_PARAGRAPH_LIMIT)
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ProfessionGeneralType getGeneralType() {
        return generalType;
    }

    public void setGeneralType(ProfessionGeneralType generalType) {
        this.generalType = generalType;
    }

}
