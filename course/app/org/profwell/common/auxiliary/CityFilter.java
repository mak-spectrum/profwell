package org.profwell.common.auxiliary;

import org.profwell.common.model.Country;
import org.profwell.ui.select.DictionaryConversionUtils;

public class CityFilter extends SingleFieldFilter {

    private Country country;

    public String getCountryValue() {
        if (country == null) {
            return DictionaryConversionUtils.EMPTY_DROP_DOWN_VALUE;
        } else {
            return String.valueOf(country);
        }
    }

    public void setCountryValue(String country) {
        for (Country c : Country.values()) {
            if (c.getName().equals(country)) {
                this.country = c;
                return;
            }
        }
        this.country = null;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
