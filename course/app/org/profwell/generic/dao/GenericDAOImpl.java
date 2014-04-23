package org.profwell.generic.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.auxiliary.GenericFilter;
import org.profwell.generic.model.Identifiable;
import org.profwell.generic.model.WorkspaceRestricted;
import org.profwell.security.model.Workspace;

import play.db.jpa.JPA;

public abstract class GenericDAOImpl<T extends Identifiable>
        implements GenericDAO<T> {

    @Override
    public long countAll() {
        CriteriaQuery<Long> cq = this.getEM().getCriteriaBuilder()
                .createQuery(Long.class);

        Root<T> root = cq.from(getEntityClass());

        cq.select(this.getEM().getCriteriaBuilder().count(root));

        return this.getEM().createQuery(cq).getSingleResult().longValue();
    }

    @Override
    public List<T> listAll() {
        CriteriaQuery<T> cq = this.getEM().getCriteriaBuilder()
                .createQuery(getEntityClass());

        cq.from(getEntityClass());

        return this.getEM().createQuery(cq).getResultList();
    }

    @Override
    public T get(long id) {
        return this.getEM().find(this.getEntityClass(), id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <WR extends Identifiable & WorkspaceRestricted> WR getFromWorkspace(
            long id, long workspaceId) {

        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());

        Root<T> entityRoot = cq.from(getEntityClass());
        Join<T, Workspace> workspaceJoin = entityRoot.join("workspace");
        cq.select(entityRoot);

        criterions.add(cb.equal(entityRoot.<Long>get("id"), id));
        criterions.add(cb.equal(workspaceJoin.<Long>get("id"), workspaceId));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        TypedQuery<T> query = getEM().createQuery(cq);

        return (WR) this.extractSingleFromList(query.getResultList());
    }

    protected <O> O extractSingleFromList(List<O> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException(list.size()
                    + " instances of "
                    + getEntityClass().getSimpleName()
                    + " found on a single line result query.");
        }
    }

    @Override
    public void save(T object) {
        this.getEM().persist(object);
    }

    @Override
    public void delete(T object) {
        this.getEM().remove(object);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected List<T> listPage(CriteriaQuery criteria, GenericFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();

        Selection<?> backUp = criteria.getSelection();

        criteria.select(cb.count(this.getRoot(criteria)));

        TypedQuery<Long> countQuery = this.getEM().createQuery(criteria);

        filter.setResultsCount(countQuery.getSingleResult());


        if (backUp != null) {
            criteria.select(backUp);
        } else {
            criteria.select(this.getRoot(criteria));
        }
        if (StringUtils.isNotBlank(filter.getOrderBy())) {
            criteria.orderBy(prepareOrderBy(criteria, filter));
        }
        TypedQuery resultQuery = this.getEM().createQuery(criteria);

        resultQuery.setFirstResult(filter.getFirstResult());
        resultQuery.setMaxResults(filter.getPageSize());

        return resultQuery.getResultList();
    }

    @SuppressWarnings("rawtypes")
    private List<Order> prepareOrderBy(CriteriaQuery criteria, GenericFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();

        List<Order> result = new ArrayList<>();

        String[] snippets = filter.getOrderBy().split(",");

        String orderBy;
        for (String snap : snippets) {
            orderBy = snap.trim();
            if (StringUtils.isNotBlank(orderBy)) {
                processSingleOrderBy(criteria, filter, cb, result);
            }
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    private void processSingleOrderBy(CriteriaQuery criteria,
            GenericFilter filter, CriteriaBuilder cb, List<Order> result) {

        String[] snippets = filter.getOrderBy().split(".");

        Path<?> orderByPath = this.getRoot(criteria);
        for (String snap : snippets) {
            orderByPath = orderByPath.get(snap);
        }

        if (filter.isOrderAsc()) {
            result.add(cb.asc(orderByPath));
        } else {
            result.add(cb.desc(orderByPath));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Root getRoot(CriteriaQuery criteria) {
        Iterator<Root> it = criteria.getRoots().iterator();

        while (it.hasNext()) {
            Root root = it.next();
            if (getEntityClass().equals(root.getJavaType())) {
                return root;
            }
        }

        return null;
    }

    protected EntityManager getEM() {
        return JPA.em();
    }

    protected abstract Class<T> getEntityClass();

}
