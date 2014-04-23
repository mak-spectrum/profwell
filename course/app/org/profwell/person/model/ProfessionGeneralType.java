package org.profwell.person.model;

import org.profwell.generic.model.Dictionary;

public enum ProfessionGeneralType implements Dictionary {

    DEVELOPER("Developer"),
    DEVOPS("DevOps"),
    HUMAN_RESOURCES("Human Resources"),
    MANAGER("Manager"),
    QUALITY_ENGINEER("Quality Engineer"),
    SALES("Sales"),
    SUPPORT("Support"),
    SYSTEM_ADMINISTRATOR("System Administrator"),
    OTHER("Other");

    private String caption;

    private ProfessionGeneralType(String caption) {
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
