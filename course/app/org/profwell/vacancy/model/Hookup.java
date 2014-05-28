package org.profwell.vacancy.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.profwell.generic.model.Identifiable;
import org.profwell.security.model.Workspace;

@Entity
@Table(name="HOOKUP")
@Access(AccessType.FIELD)
public class Hookup implements Identifiable, HookupData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false)
    private long id = DEFAULT_UNINITIALIZED_ID_VALUE;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS", nullable = false, length = 30)
    private HookupStatus status;

    @Temporal(TemporalType.DATE)
    @Column(name="CONTACTED_ON", nullable = false)
    private Date contactedOn;

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private HookupPerson candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VACANCY_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Vacancy vacancy;

    @Embedded
    private EngagementSource engagementSource;

    /**
     * For simplification.
     * TODO : refactor me
     */
    @Embedded
    private HookupDocuments documents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSPACE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Workspace workspace;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_ACTIVITY_ON", nullable = false)
    private Date lastActivityOn;

    /**
     * For simplification.
     * TODO : refactor me
     */
    @Column(name = "PASSED_TEST_TASK_STATUS", nullable = false)
    private boolean passedTestTaskStatus;

    @Column(name = "ARCHIVED", nullable = false)
    private boolean archived;

    @Override
    public boolean isCanBeArchived() {
        return this.status.isCanBeArchived();
    }

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

    public Vacancy getVacancy() {
        return this.vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    @Override
    public HookupStatus getStatus() {
        return this.status;
    }

    public void setStatus(HookupStatus status) {
        this.status = status;
    }

    public EngagementSource getEngagementSource() {
        return this.engagementSource;
    }

    public void setEngagementSource(EngagementSource engagementSource) {
        this.engagementSource = engagementSource;
    }

    public Date getLastActivityOn() {
        return this.lastActivityOn;
    }

    public void setLastActivityOn(Date lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Date getContactedOn() {
        return this.contactedOn;
    }

    public void setContactedOn(Date contactedOn) {
        this.contactedOn = contactedOn;
    }

    public Workspace getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    @Override
    public boolean isArchived() {
        return this.archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean isPassedTestTaskStatus() {
        return this.passedTestTaskStatus;
    }

    public void setPassedTestTaskStatus(boolean passedTestTaskStatus) {
        this.passedTestTaskStatus = passedTestTaskStatus;
    }

    public HookupDocuments getDocuments() {
        return this.documents;
    }

    public void setDocuments(HookupDocuments documents) {
        this.documents = documents;
    }

    public HookupPerson getCandidate() {
        return this.candidate;
    }

    public void setCandidate(HookupPerson candidate) {
        this.candidate = candidate;
    }

}
