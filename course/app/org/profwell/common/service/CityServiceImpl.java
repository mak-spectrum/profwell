package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.dao.CityDAO;
import org.profwell.common.model.City;
import org.profwell.common.model.Country;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;

public class CityServiceImpl
        extends GenericServiceImpl<CityDAO, City>
        implements CityService {

    @Override
    public List<City> listCity(CityFilter filter) {
        return dao.listCity(filter);
    }

    @Override
    public void addUniqueDictionaryValue(Country country,
            String cityName, Workspace workspace) {

        if (dao.countWithName(cityName, workspace.getId()) == 0) {
            City city = new City();
            city.setName(cityName);
            city.setCountry(country);
            city.setWorkspace(workspace);
            dao.save(city);
        }

    }
}
