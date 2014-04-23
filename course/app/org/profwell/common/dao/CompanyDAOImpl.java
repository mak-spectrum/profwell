package org.profwell.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Company;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.Workspace;

public class CompanyDAOImpl extends GenericDAOImpl<Company>
        implements CompanyDAO {

    @Override
    public List<Company> listCompany(SingleFieldFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Company> cq = cb.createQuery(getEntityClass());
        Root<Company> companyRoot = cq.from(getEntityClass());
        cq.select(companyRoot);
        Join<Company, Workspace> workspaceJoin = companyRoot.join("workspace");

        if (StringUtils.isNotBlank(filter.getValue())) {
            criterions.add(
                    cb.like(
                            cb.lower(companyRoot.<String>get("name")),
                            filter.getValue().toLowerCase() + "%"));
        }

        criterions.add(cb.equal(workspaceJoin.<Long>get("id"),
                filter.getWorkspaceId()));

        if (criterions.size() > 0) {
            cq.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return this.listPage(cq, filter);
    }

    @Override
    public long countWithName(String name, long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Company> root = cq.from(getEntityClass());
        Join<Company, Workspace> workspaceJoin = root.join("workspace");

        cq.select(cb.count(root));

        criterions.add(cb.equal(root.<Long>get("name"), name));
        criterions.add(cb.equal(workspaceJoin.<Long>get("id"), workspaceId));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        return this.getEM().createQuery(cq).getSingleResult().longValue();
    }

    @Override
    protected Class<Company> getEntityClass() {
        return Company.class;
    }

}
