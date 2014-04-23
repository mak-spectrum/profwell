package org.profwell.common.service;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.dao.CompanyDAO;
import org.profwell.common.model.Company;
import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.security.model.Workspace;

public class CompanyServiceImpl
        extends GenericServiceImpl<CompanyDAO, Company>
        implements CompanyService {

    @Override
    public List<Company> listCompany(SingleFieldFilter filter) {
        return dao.listCompany(filter);
    }

    @Override
    public void addUniqueDictionaryValue(String name, String details,
            String socialBenefits, Workspace workspace) {

        if (dao.countWithName(name, workspace.getId()) == 0) {
            Company company = new Company();
            company.setName(name);
            company.setDetails(details);
            company.setSocialBenefits(socialBenefits);
            company.setWorkspace(workspace);
            dao.save(company);
        }

    }
}
