package org.profwell.vacancy.domain;

import java.util.ArrayList;
import java.util.List;


public class VacancyViewForm extends AbstractVacancyForm {

    public static final String MAIN_TAB = "vacancyInfoTab";

    public static final String HOOKUPS_TAB = "hookupsTab";

    public static final String VACANCY_SHARING_TAB = "vacancySharingTab";

    /**
     * Active tab on UI. Manages content loading.
     */
    private String activeTab = MAIN_TAB;

    private boolean sharingTabAvailable;

    private boolean editAvailable;

    private List<HookupDTO> hookups = new ArrayList<>();

    public String getVacancyInfoTabActivity() {
        if (MAIN_TAB.equals(this.activeTab)) {
            return "active";
        } else {
            return "";
        }
    }

    public String getHookupsTabActivity() {
        if (HOOKUPS_TAB.equals(this.activeTab)) {
            return "active";
        } else {
            return "";
        }
    }

    public String getVacancySharingTabActivity() {
        if (VACANCY_SHARING_TAB.equals(this.activeTab)) {
            return "active";
        } else {
            return "";
        }
    }

    public boolean isHookupsTabActive() {
        return HOOKUPS_TAB.equals(this.activeTab);
    }

    /* SIMPLE SETTERS/GETTERS */



    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public List<HookupDTO> getHookups() {
        return hookups;
    }

    public void setHookups(List<HookupDTO> hookups) {
        this.hookups = hookups;
    }

    public boolean isSharingTabAvailable() {
        return sharingTabAvailable;
    }

    public void setSharingTabAvailable(boolean sharingTabAvailable) {
        this.sharingTabAvailable = sharingTabAvailable;
    }

    public boolean isEditAvailable() {
        return editAvailable;
    }

    public void setEditAvailable(boolean editAvailable) {
        this.editAvailable = editAvailable;
    }

}
