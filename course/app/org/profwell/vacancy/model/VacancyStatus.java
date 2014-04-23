package org.profwell.vacancy.model;

import org.profwell.generic.model.Dictionary;

public enum VacancyStatus implements Dictionary {

    OPENED("Opened"),
    SUSPENDED("Suspended"),
    CLOSED("Closed");

    private String caption;

    private VacancyStatus(String caption) {
        this.caption = caption;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    public static Dictionary[] getValues() {
        return values();
    }

}