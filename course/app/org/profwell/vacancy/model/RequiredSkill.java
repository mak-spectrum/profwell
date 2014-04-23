package org.profwell.vacancy.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;

@Entity
@Table(name="VACANCY_REQUIRED_SKILL")
@Access(AccessType.FIELD)
public class RequiredSkill implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VACANCY_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Vacancy vacancy;

    @Column(name="NAME", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String name;

    @Column(name = "SKILL_INDEX", nullable = true)
    private int index;

    @Column(name = "MANDATORY", nullable = false)
    private boolean mandatory;

    @Override
    public boolean isNew() {
        return this.id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
