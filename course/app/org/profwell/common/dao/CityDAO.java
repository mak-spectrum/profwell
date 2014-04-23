package org.profwell.common.dao;

import java.util.List;

import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.model.City;
import org.profwell.generic.dao.GenericDAO;

public interface CityDAO extends GenericDAO<City> {

    List<City> listCity(CityFilter filter);

    long countWithName(String name, long workspaceId);

}
