package org.profwell.vacancy.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;
import org.profwell.person.domain.PersonInfoHolder;
import org.profwell.person.model.Profession;

@Entity
@Table(name="HOOKUP_PERSON")
@Access(AccessType.FIELD)
public class HookupPerson implements Identifiable, PersonInfoHolder {

    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(
                    name = "property",
                    value = "hookup"))
    @GeneratedValue(generator = "generator")
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Hookup hookup;

    @Column(name="FIRST_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String firstName;

    @Column(name="SECOND_NAME", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String secondName;

    @Column(name="LAST_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String lastName;

    @Column(name="SHORT_NOTE", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String shortNote;

    @Column(name="CURRENT_POSITION", nullable = true,
            length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String currentPosition;

    @Column(name="CURRENT_COMPANY", nullable = true,
            length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String currentCompany;

    @Embedded
    private Profession profession;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL},
            targetEntity = HookupPersonContactData.class)
    @OrderColumn(name = "CONTACT_INDEX")
    @JoinColumn(name = "HOOKUP_PERSON_ID",
            referencedColumnName = "ID",
            nullable = true)
    private List<HookupPersonContactData> contactDataRecords = new ArrayList<>();

    @OneToMany(
            targetEntity = HookupPerson.class)
    @JoinColumn(name = "HOOKUP_PERSON_ID",
            referencedColumnName = "ID",
            nullable = true)
    private List<HookupPersonLink> references = new ArrayList<>();

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getPersonFirstName() {
        return this.firstName;
    }

    @Override
    public String getPersonSecondName() {
        return this.secondName;
    }

    @Override
    public String getPersonLastName() {
        return this.lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getShortNote() {
        return this.shortNote;
    }

    public void setShortNote(String shortNote) {
        this.shortNote = shortNote;
    }

    public String getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCurrentCompany() {
        return this.currentCompany;
    }

    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

}
