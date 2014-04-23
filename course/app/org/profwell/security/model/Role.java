package org.profwell.security.model;

import org.profwell.generic.model.Dictionary;


public enum Role implements Dictionary {

    ADMINISTRATOR("Administrator"),
    HR_MANAGER("HR Manager");

    private String caption;

    private Role(String caption) {
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
