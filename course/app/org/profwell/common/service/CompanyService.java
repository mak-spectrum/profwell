package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Company;
import org.profwell.generic.service.GenericService;
import org.profwell.security.model.Workspace;

public interface CompanyService extends GenericService<Company> {

    List<Company> listCompany(SingleFieldFilter filter);

    void addUniqueDictionaryValue(String name, String details,
            String socialBenefits, Workspace workspace);
}
