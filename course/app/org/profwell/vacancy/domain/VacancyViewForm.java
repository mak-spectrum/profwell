package org.profwell.vacancy.domain;

import java.util.ArrayList;
import java.util.List;


public class VacancyViewForm extends AbstractVacancyForm {

    public static final String MAIN_TAB = "vacancyInfoTab";

    public static final String HOOKUPS_TAB = "hookupsTab";

    /**
     * Active tab on UI. Manages content loading.
     */
    private String activeTab = MAIN_TAB;

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

}
