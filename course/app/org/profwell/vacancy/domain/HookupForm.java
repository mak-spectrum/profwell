package org.profwell.vacancy.domain;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.profwell.generic.domain.FormatsHolder;
import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;
import org.profwell.person.model.Person;
import org.profwell.ui.select.DictionaryConversionUtils;
import org.profwell.vacancy.model.EngagementSource;
import org.profwell.vacancy.model.EngagementType;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.model.Vacancy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.PersonUtils;

public class HookupForm extends ValidationForm {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(HookupForm.class);

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private long vacancyId;

    private Date contactedOn;
    private String contactedOnValue;

    private String candidateCurrentPosition;
    private String candidateCurrentCompany;

    private long candidateId;
    private String candidateName;

    private HookupStatus status;

    private EngagementType engagementType;

    private long recommenderId;
    private String recommenderName;

    private String engagementSourceNote;

    private Date lastActivityOn;

    public void transferFrom(Hookup hookup) {

        this.id = hookup.getId();

        this.contactedOn = hookup.getContactedOn();
        this.contactedOnValue = FormatsHolder.DATE_FORMATTER
                .format(hookup.getContactedOn());

        this.candidateId = hookup.getCandidate().getId();
        this.candidateName = PersonUtils.getFullName(hookup.getCandidate());
        this.candidateCurrentPosition = hookup.getCandidateCurrentPosition();
        this.candidateCurrentCompany = hookup.getCandidateCurrentCompany();

        this.vacancyId = hookup.getVacancy().getId();

        this.engagementType = hookup.getEngagementSource().getType();
        this.engagementSourceNote =
                hookup.getEngagementSource().getSourceNote();

        if (hookup.getEngagementSource().getPerson() != null) {
            this.recommenderId = hookup.getEngagementSource()
                    .getPerson().getId();
            this.recommenderName = PersonUtils.getFullName(
                    hookup.getEngagementSource().getPerson());
        }

    }

    public void transferTo(Hookup hookup) {

        hookup.setContactedOn(this.contactedOn);

        if (hookup.getEngagementSource() == null) {
            hookup.setEngagementSource(new EngagementSource());
        }
        hookup.getEngagementSource().setType(this.engagementType);
        hookup.getEngagementSource().setSourceNote(this.engagementSourceNote);

        Person candidate = new Person();
        candidate.setId(this.candidateId);
        hookup.setCandidate(candidate);
        hookup.setCandidateCurrentPosition(this.candidateCurrentPosition);
        hookup.setCandidateCurrentCompany(this.candidateCurrentCompany);

        if (this.recommenderId >
                Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE) {
            Person recommender = new Person();
            recommender.setId(this.recommenderId);
            hookup.getEngagementSource().setPerson(recommender);
        }

        Vacancy vacancy = new Vacancy();
        vacancy.setId(this.vacancyId);
        hookup.setVacancy(vacancy);

    }

    public boolean isMainSectionExpanded() {
        return true;
    }

    public boolean isEngagementSectionExpanded() {
        if (isNew()) {
            return true;
        }

        if (getVc().isEmpty()) {
            return false;
        } else {
            return isBlockHasValidationMessages(
                    "engagementTypeValue",
                    "engagementPerson",
                    "engagementSourceNote");
        }
    }

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String idvalue) {
        this.id = Long.parseLong(idvalue);
    }

    public String getVacancyIdValue() {
        return String.valueOf(this.vacancyId);
    }

    public void setVacancyIdValue(String vacancyIdvalue) {
        this.vacancyId = Long.parseLong(vacancyIdvalue);
    }

    public String getContactedOnFormatted() {
        if (this.contactedOn == null) {
            return "";
        } else {
            return FormatsHolder.DATE_FORMATTER.format(this.contactedOn);
        }
    }

    public void setContactedOnValue(String contactedOnValue) {
        this.contactedOnValue = contactedOnValue;
        try {
            this.contactedOn = DateUtils.parseDate(contactedOnValue,
                    FormatsHolder.DATE_FORMAT);
        } catch (ParseException pe) {
            LOGGER.debug("Hookup contact on date parse exception.", pe);
        }
    }

    public String getCandidateIdValue() {
        return String.valueOf(this.candidateId);
    }

    public void setCandidateIdValue(String candidateIdvalue) {
        this.candidateId = Long.parseLong(candidateIdvalue);
    }

    public String getRecommenderIdValue() {
        return String.valueOf(this.recommenderId);
    }

    public void setRecommenderIdValue(String recommenderIdvalue) {
        this.recommenderId = Long.parseLong(recommenderIdvalue);
    }

    public String getLastActivityOnFormatted(String lastActivityOn) {
        return FormatsHolder.TIME_FORMATTER.format(this.lastActivityOn);
    }

    public String getEngagementTypeValue() {
        if (this.engagementType == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return this.engagementType.getName();
        }
    }

    public void setEngagementTypeValue(String type) {
        for (EngagementType t : EngagementType.values()) {
            if (t.getName().equals(type)) {
                this.engagementType = t;
                return;
            }
        }
        this.engagementType = null;
    }

    /* SIMPLE SETTERS/GETTERS */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(long vacancyId) {
        this.vacancyId = vacancyId;
    }

    public long getRecommenderId() {
        return recommenderId;
    }

    public void setRecommenderId(long recommenderId) {
        this.recommenderId = recommenderId;
    }

    public HookupStatus getStatus() {
        return status;
    }

    public void setStatus(HookupStatus status) {
        this.status = status;
    }

    public String getEngagementSourceNote() {
        return engagementSourceNote;
    }

    public void setEngagementSourceNote(String engagementSourceNote) {
        this.engagementSourceNote = engagementSourceNote;
    }

    public String getContactedOnValue() {
        return contactedOnValue;
    }

    public Date getContactedOn() {
        return contactedOn;
    }

    public long getCandidateId() {
        return candidateId;
    }

    public void setContactedOn(Date contactedOn) {
        this.contactedOn = contactedOn;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getRecommenderName() {
        return recommenderName;
    }

    public void setRecommenderName(String recommenderName) {
        this.recommenderName = recommenderName;
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

}
