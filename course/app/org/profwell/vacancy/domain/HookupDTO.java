package org.profwell.vacancy.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.model.Identifiable;
import org.profwell.person.domain.PersonInfoHolder;
import org.profwell.vacancy.model.HookupData;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.web.HookupStatusControl;

import utils.DateTimeFormatUtils;

public class HookupDTO implements PersonInfoHolder, HookupData {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String personFirstName;

    private String personSecondName;

    private String personLastName;

    private String personCurrentPosition;

    private String personCurrentCompany;

    private HookupStatus status;

    private boolean passedTestTaskStatus;

    private Date contactedOn;

    private Date lastUpdateDate;

    private boolean archived;

    private String attachableDocumentTitle;

    private List<HookupStatusControl> statusMoves = new ArrayList<>();

    private HookupDocumentDTO resume;

    private HookupDocumentDTO testtask;

    private List<HookupDocumentDTO> interviewFeedbacks = new ArrayList<>();

    private List<HookupDocumentDTO> probationFeedbacks = new ArrayList<>();

    @Override
    public boolean isCanBeArchived() {
        return this.status.isCanBeArchived();
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    public String getLastUpdateDateFormatted() {
        return DateTimeFormatUtils.getDatetimeFormatted(this.lastUpdateDate);
    }

    public boolean isProfessionalInfoNotEmpty() {
        return this.isPositionInfoNotEmpty()
                || this.isCompanyInfoNotEmpty();
    }

    public boolean isPositionInfoNotEmpty() {
        return StringUtils.isNotBlank(personCurrentPosition);
    }

    public boolean isCompanyInfoNotEmpty() {
        return StringUtils.isNotBlank(personCurrentCompany);
    }

    public boolean isDocumentAttachable() {
        return status.isDocumentAttachable();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    @Override
    public String getPersonSecondName() {
        return personSecondName;
    }

    public void setPersonSecondName(String personSecondName) {
        this.personSecondName = personSecondName;
    }

    @Override
    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    @Override
    public HookupStatus getStatus() {
        return status;
    }

    public void setStatus(HookupStatus status) {
        this.status = status;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getContactedOn() {
        return contactedOn;
    }

    public void setContactedOn(Date contactedOn) {
        this.contactedOn = contactedOn;
    }

    public String getPersonCurrentPosition() {
        return personCurrentPosition;
    }

    public void setPersonCurrentPosition(String personCurrentPosition) {
        this.personCurrentPosition = personCurrentPosition;
    }

    public String getPersonCurrentCompany() {
        return personCurrentCompany;
    }

    public void setPersonCurrentCompany(String personCurrentCompany) {
        this.personCurrentCompany = personCurrentCompany;
    }

    public List<HookupStatusControl> getStatusMoves() {
        return statusMoves;
    }

    public void setStatusMoves(List<HookupStatusControl> statusMoves) {
        this.statusMoves = statusMoves;
    }

    @Override
    public boolean isPassedTestTaskStatus() {
        return passedTestTaskStatus;
    }

    public void setPassedTestTaskStatus(boolean passedTestTaskStatus) {
        this.passedTestTaskStatus = passedTestTaskStatus;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getAttachableDocumentTitle() {
        return attachableDocumentTitle;
    }

    public void setAttachableDocumentTitle(String attachableDocumentTitle) {
        this.attachableDocumentTitle = attachableDocumentTitle;
    }

    public HookupDocumentDTO getResume() {
        return resume;
    }

    public void setResume(HookupDocumentDTO resume) {
        this.resume = resume;
    }

    public HookupDocumentDTO getTesttask() {
        return testtask;
    }

    public void setTesttask(HookupDocumentDTO testtask) {
        this.testtask = testtask;
    }

    public List<HookupDocumentDTO> getInterviewFeedbacks() {
        return interviewFeedbacks;
    }

    public void setInterviewFeedbacks(List<HookupDocumentDTO> interviewFeedbacks) {
        this.interviewFeedbacks = interviewFeedbacks;
    }

    public List<HookupDocumentDTO> getProbationFeedbacks() {
        return probationFeedbacks;
    }

    public void setProbationFeedbacks(List<HookupDocumentDTO> probationFeedbacks) {
        this.probationFeedbacks = probationFeedbacks;
    }

}
