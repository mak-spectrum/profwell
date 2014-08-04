package org.profwell.dashboard.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.profwell.generic.model.Identifiable;
import org.profwell.person.domain.PersonInfoHolder;
import org.profwell.vacancy.model.HookupStatus;

public class DashboardHookupDTO implements PersonInfoHolder {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm", Locale.US);

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String personFirstName;

    private String personSecondName;

    private String personLastName;

    private String currentPosition;

    private String currentCompany;

    private HookupStatus status;

    private Date lastUpdateDate;

    public String getLastUpdateDateFormatted() {
        return TIME_FORMAT.format(this.lastUpdateDate);
    }

    /* SIMPLE SETTERS/GETTERS */

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


    public String getCurrentPosition() {
        return currentPosition;
    }


    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }


    public String getCurrentCompany() {
        return currentCompany;
    }


    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }


    public HookupStatus getStatus() {
        return status;
    }


    public void setStatus(HookupStatus status) {
        this.status = status;
    }


    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }


    public void setLastActivityOn(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
