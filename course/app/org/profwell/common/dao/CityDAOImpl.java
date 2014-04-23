package org.profwell.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.model.City;
import org.profwell.common.model.Country;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.Workspace;

public class CityDAOImpl extends GenericDAOImpl<City>
        implements CityDAO {

    @Override
    public List<City> listCity(CityFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<City> cq = cb.createQuery(getEntityClass());
        Root<City> cityRoot = cq.from(getEntityClass());
        cq.select(cityRoot);
        Join<City, Workspace> workspaceJoin = cityRoot.join("workspace");

        if (StringUtils.isNotBlank(filter.getValue())) {
            criterions.add(
                    cb.like(
                            cb.lower(cityRoot.<String>get("name")),
                            filter.getValue().toLowerCase() + "%"));
        }

        if (filter.getCountry() != null) {
            criterions.add(cb.equal(cityRoot.<Country>get("country"),
                    filter.getCountry()));
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

        Root<City> root = cq.from(getEntityClass());
        Join<City, Workspace> workspaceJoin = root.join("workspace");

        cq.select(cb.count(root));

        criterions.add(cb.equal(root.<Long>get("name"), name));
        criterions.add(cb.equal(workspaceJoin.<Long>get("id"), workspaceId));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        return this.getEM().createQuery(cq).getSingleResult().longValue();
    }

    @Override
    protected Class<City> getEntityClass() {
        return City.class;
    }

}
