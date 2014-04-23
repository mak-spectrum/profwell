package org.profwell.common.model;

import org.profwell.generic.model.Dictionary;

public enum Country implements Dictionary {

    UA("Ukraine"),
    RU("Russia"),
    BY("Byelorussia"),
    MD("Moldova"),
    US("United States"),
    EU("European Union"),
    OTHER("Another");

    private String caption;

    private Country(String caption) {
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