package org.profwell.dashboard.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.profwell.generic.model.Identifiable;
import org.profwell.vacancy.model.VacancyPriority;

public class DashboardVacancyDTO {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd MMM yyyy", Locale.US);

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String positionCaption;

    private String projectName;

    private String companyName;

    private VacancyPriority priority;

    private Date openingDate;

    private Date dueDate;

    private List<DashboardHookupDTO> hookups = new ArrayList<>();

    public String getOpeningDateFormatted() {
        return FORMAT.format(this.openingDate);
    }

    public String getDueDateFormatted() {
        return FORMAT.format(this.dueDate);
    }

    /* SIMPLE SETTERS/GETTERS */

    public String getPositionCaption() {
        return positionCaption;
    }

    public void setPositionCaption(String positionCaption) {
        this.positionCaption = positionCaption;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public VacancyPriority getPriority() {
        return priority;
    }

    public void setPriority(VacancyPriority priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<DashboardHookupDTO> getHookups() {
        return hookups;
    }

    public void setHookups(List<DashboardHookupDTO> hookups) {
        this.hookups = hookups;
    }
}
