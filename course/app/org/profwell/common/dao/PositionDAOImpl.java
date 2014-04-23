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
import org.profwell.common.model.Position;
import org.profwell.common.model.Skill;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.Workspace;

public class PositionDAOImpl extends GenericDAOImpl<Position>
        implements PositionDAO {

    @Override
    public List<Position> listPosition(SingleFieldFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Position> cq = cb.createQuery(getEntityClass());
        Root<Position> positionRoot = cq.from(getEntityClass());
        cq.select(positionRoot);
        Join<Skill, Workspace> workspaceJoin = positionRoot.join("workspace");

        if (StringUtils.isNotBlank(filter.getValue())) {
            criterions.add(
                    cb.like(
                            cb.lower(positionRoot.<String>get("name")),
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

        Root<Position> root = cq.from(getEntityClass());
        Join<Position, Workspace> workspaceJoin = root.join("workspace");

        cq.select(cb.count(root));

        criterions.add(cb.equal(root.<Long>get("name"), name));
        criterions.add(cb.equal(workspaceJoin.<Long>get("id"), workspaceId));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        return this.getEM().createQuery(cq).getSingleResult().longValue();
    }

    @Override
    protected Class<Position> getEntityClass() {
        return Position.class;
    }

}
