package org.profwell.common.dao;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Company;
import org.profwell.generic.dao.GenericDAO;

public interface CompanyDAO extends GenericDAO<Company> {

    List<Company> listCompany(SingleFieldFilter filter);

    long countWithName(String name, long workspaceId);

}
