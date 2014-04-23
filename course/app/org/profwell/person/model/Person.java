package org.profwell.person.model;

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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.profwell.generic.model.ModelConstants;
import org.profwell.generic.model.WorkspaceRestricted;
import org.profwell.person.domain.PersonInfoHolder;
import org.profwell.security.model.Workspace;

@Entity
@Table(name="PERSON")
@Access(AccessType.FIELD)
public class Person implements WorkspaceRestricted, PersonInfoHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Column(name="FIRST_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String firstName;

    @Column(name="SECOND_NAME", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String secondName;

    @Column(name="LAST_NAME", nullable = false, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String lastName;

    @Column(name="SHORT_NOTE", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String shortNote;

    @Embedded
    private Profession profession;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL},
            targetEntity = ContactData.class)
    @OrderColumn(name = "CONTACT_INDEX")
    @JoinColumn(name = "PERSON_ID",
            referencedColumnName = "ID",
            nullable = true)
    private List<ContactData> contactDataRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Workspace workspace;

    @Override
    public boolean isNew() {
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
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

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ContactData> getContactDataRecords() {
        return contactDataRecords;
    }

    public void setContactDataRecords(List<ContactData> contactDataRecords) {
        this.contactDataRecords = contactDataRecords;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getShortNote() {
        return shortNote;
    }

    public void setShortNote(String shortNote) {
        this.shortNote = shortNote;
    }

}
