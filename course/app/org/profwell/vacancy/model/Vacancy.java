package org.profwell.vacancy.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.profwell.common.model.Country;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;
import org.profwell.security.model.Workspace;

@Entity
@Table(name="VACANCY")
@Access(AccessType.FIELD)
public class Vacancy implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Embedded
    private VacancyCompany company;

    @Embedded
    private VacancyPosition position;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS", nullable = false, length = 20)
    private VacancyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="PRIORITY", nullable = false, length = 20)
    private VacancyPriority priority;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="OPENING_DATE", nullable = false)
    private Date openingDatetime;

    @Temporal(TemporalType.DATE)
    @Column(name="DUE_DATE", nullable = true)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CLOSING_DATE", nullable = true)
    private Date closingDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name="COUNTRY", nullable = true, length = 5)
    private Country country;

    @Column(name="CITY", nullable = true, length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String city;

    @Embedded
    private SalaryRange salaryRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Workspace workspace;

    @OneToMany(
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            targetEntity = Hookup.class)
    private Set<Hookup> hookups = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private VacancySharingConfiguration sharingConfiguration;

    @Override
    public boolean isNew() {
        return this.id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public VacancyCompany getCompany() {
        return this.company;
    }

    public void setCompany(VacancyCompany company) {
        this.company = company;
    }

    public Date getOpeningDatetime() {
        return this.openingDatetime;
    }

    public void setOpeningDatetime(Date openingDatetime) {
        this.openingDatetime = openingDatetime;
    }

    public Date getClosingDatetime() {
        return this.closingDatetime;
    }

    public void setClosingDatetime(Date closingDatetime) {
        this.closingDatetime = closingDatetime;
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

    public SalaryRange getSalaryRange() {
        return this.salaryRange;
    }

    public void setSalaryRange(SalaryRange salaryRange) {
        this.salaryRange = salaryRange;
    }

    public Workspace getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public VacancyStatus getStatus() {
        return this.status;
    }

    public void setStatus(VacancyStatus status) {
        this.status = status;
    }

    public VacancyPosition getPosition() {
        return this.position;
    }

    public void setPosition(VacancyPosition position) {
        this.position = position;
    }

    public VacancyPriority getPriority() {
        return this.priority;
    }

    public void setPriority(VacancyPriority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Set<Hookup> getHookups() {
        return this.hookups;
    }

    public void setHookups(Set<Hookup> hookups) {
        this.hookups = hookups;
    }

    public VacancySharingConfiguration getSharingConfiguration() {
        return this.sharingConfiguration;
    }

    public void setSharingConfiguration(
            VacancySharingConfiguration sharingConfiguration) {
        this.sharingConfiguration = sharingConfiguration;
    }

}
