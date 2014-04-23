package org.profwell.vacancy.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.profwell.generic.model.ModelConstants;
import org.profwell.person.model.Person;

@Embeddable
@Access(AccessType.FIELD)
public class EngagementSource {

    @Enumerated(EnumType.STRING)
    @Column(name="ENGAGEMENT_TYPE", nullable = false, length = 50)
    private EngagementType type;


    @ManyToOne
    @JoinColumn(name = "ENGAGEMENT_PERSON_ID")
    private Person person;

    @Column(name="ENGAGEMENT_SOURCE_NOTE", nullable = true,
            length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String sourceNote;

    public EngagementType getType() {
        return type;
    }

    public void setType(EngagementType type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }

}
