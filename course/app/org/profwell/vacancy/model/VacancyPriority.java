package org.profwell.vacancy.model;

import org.profwell.generic.model.Dictionary;

public enum VacancyPriority implements Dictionary {

    URGENT("Urgent"),
    HIGH("High"),
    LOW("Low"),
    BACKGROUND("Background");

    private String caption;

    private VacancyPriority(String caption) {
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