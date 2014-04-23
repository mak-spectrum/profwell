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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.ModelConstants;
import org.profwell.person.model.Person;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CANDIDATE_ID",
            referencedColumnName = "ID",
            nullable = false)
    private Person candidate;

    @Column(name="CANDIDATE_CURRENT_POSITION", nullable = true,
            length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String candidateCurrentPosition;

    @Column(name="CANDIDATE_CURRENT_COMPANY", nullable = true,
            length = ModelConstants.STANDARD_TEXT_LIMIT)
    private String candidateCurrentCompany;

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
        return id == DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    @Override
    public HookupStatus getStatus() {
        return status;
    }

    public void setStatus(HookupStatus status) {
        this.status = status;
    }

    public EngagementSource getEngagementSource() {
        return engagementSource;
    }

    public void setEngagementSource(EngagementSource engagementSource) {
        this.engagementSource = engagementSource;
    }

    public Date getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(Date lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Date getContactedOn() {
        return contactedOn;
    }

    public void setContactedOn(Date contactedOn) {
        this.contactedOn = contactedOn;
    }

    public Person getCandidate() {
        return candidate;
    }

    public void setCandidate(Person candidate) {
        this.candidate = candidate;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getCandidateCurrentPosition() {
        return candidateCurrentPosition;
    }

    public void setCandidateCurrentPosition(String candidateCurrentPosition) {
        this.candidateCurrentPosition = candidateCurrentPosition;
    }

    public String getCandidateCurrentCompany() {
        return candidateCurrentCompany;
    }

    public void setCandidateCurrentCompany(String candidateCurrentCompany) {
        this.candidateCurrentCompany = candidateCurrentCompany;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public boolean isPassedTestTaskStatus() {
        return passedTestTaskStatus;
    }

    public void setPassedTestTaskStatus(boolean passedTestTaskStatus) {
        this.passedTestTaskStatus = passedTestTaskStatus;
    }

    public HookupDocuments getDocuments() {
        return documents;
    }

    public void setDocuments(HookupDocuments documents) {
        this.documents = documents;
    }

}
