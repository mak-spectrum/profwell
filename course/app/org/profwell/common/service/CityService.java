package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.model.City;
import org.profwell.common.model.Country;
import org.profwell.generic.service.GenericService;
import org.profwell.security.model.Workspace;

public interface CityService extends GenericService<City> {

    List<City> listCity(CityFilter filter);

    void addUniqueDictionaryValue(Country country, String cityName,
            Workspace workspace);
}
