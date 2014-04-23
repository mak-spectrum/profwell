package org.profwell.person.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.model.Person;
import org.profwell.security.model.Workspace;

public class PersonDAOImpl extends GenericDAOImpl<Person> implements PersonDAO {

    @Override
    public List<Person> listPerson(PersonFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Person> criteria = cb.createQuery(getEntityClass());
        Root<Person> person = criteria.from(getEntityClass());
        Join<Person, Workspace> workspace = person.join("workspace");

        criteria.select(person);

        if (StringUtils.isNotBlank(filter.getName())) {
            criterions.add(
                    cb.or(
                            cb.like(person.<String>get("firstName"), filter.getName() + "%"),
                            cb.like(person.<String>get("lastName"), filter.getName() + "%")));
        }

        if (filter.getProfession() != null) {
            criterions.add(cb.equal(person.get("profession").get("generalType"),
                    filter.getProfession()));
        }

        criterions.add(cb.equal(workspace.<Long>get("id"),
                filter.getWorkspaceId()));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return this.listPage(criteria, filter);
    }

    @Override
    public List<Person> listPersonByName(
            String nameFragment, long workspaceId) {
        return null;
    }

    @Override
    public List<Long> checkBelongToWorkspace(List<Long> personIds,
            long workspaceId) {

        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Person> person = criteria.from(getEntityClass());
        Join<Person, Workspace> workspace = person.join("workspace");

        criteria.multiselect(person.get("id"));

        criterions.add(cb.equal(workspace.<Long>get("id"), workspaceId));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Long> resultQuery = this.getEM().createQuery(criteria);

        return resultQuery.getResultList();
    }

    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }

}
