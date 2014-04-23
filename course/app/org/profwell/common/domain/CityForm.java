package org.profwell.common.domain;

import org.profwell.common.model.City;
import org.profwell.common.model.Country;
import org.profwell.generic.domain.ValidationForm;
import org.profwell.generic.model.Identifiable;
import org.profwell.ui.select.DictionaryConversionUtils;

public class CityForm extends ValidationForm {

    private long id = Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;

    private String name;
    private Country country;

    public void transferFrom(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.country = city.getCountry();
    }

    public void transferTo(City city) {
        city.setName(this.name);
        city.setCountry(country);
    }

    public boolean isNew() {
        return this.id == Identifiable.DEFAULT_UNINITIALIZED_ID_VALUE;
    }

    public String getIdValue() {
        return String.valueOf(this.id);
    }

    public void setIdValue(String idvalue) {
        this.id = Long.parseLong(idvalue);
    }

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

    /* SIMPLE SETTERS/GETTERS */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
