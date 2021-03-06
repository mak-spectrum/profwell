package org.profwell.vacancy.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.profwell.generic.model.Identifiable;
import org.profwell.security.model.User;

@Entity
@Table(name="VACANCY_SHARING_CONFIGURATION")
@Access(AccessType.FIELD)
public class VacancySharingConfiguration implements Identifiable {

    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(
                    name = "property",
                    value = "vacancy"))
    @GeneratedValue(generator = "generator")
    @Column(name="ID", unique = true, nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VACANCY_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Vacancy vacancy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESPONSIBLE_ID",
            referencedColumnName = "ID",
            nullable = true)
    private User responsible;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            targetEntity = VacancySharingRecord.class)
    private Set<VacancySharingRecord> records = new HashSet<>();

    public VacancySharingRecord getRecordForPartner(Long partnerId) {
        for (VacancySharingRecord record : this.records) {
            if (record.getPartner().getId() == partnerId) {
                return record;
            }
        }

        return null;
    }

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

    public Vacancy getVacancy() {
        return this.vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public User getResponsible() {
        return this.responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public Set<VacancySharingRecord> getRecords() {
        return this.records;
    }

    public void setRecords(Set<VacancySharingRecord> records) {
        this.records = records;
    }
}
