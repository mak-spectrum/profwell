package org.profwell.vacancy.model;

import org.profwell.generic.model.Dictionary;

public enum Currency implements Dictionary {

    USD("$"),
    EUR("€"),
    RUR("Ruble"),
    UAH("₴");

    private String caption;

    private Currency(String caption) {
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
