package org.profwell.vacancy.model;

import org.profwell.generic.model.Dictionary;

public enum EngagementType implements Dictionary {

    ACTIVE_SEARCH("Active Search"),
    PASSIVE_SEARCH("Passive Search"),
    OTHER("Other");

    private String caption;

    private EngagementType(String caption) {
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
