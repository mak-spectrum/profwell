package org.profwell.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.Skill;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.Workspace;

public class SkillDAOImpl extends GenericDAOImpl<Skill>
        implements SkillDAO {

    @Override
    public List<Skill> listSkill(SingleFieldFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Skill> cq = cb.createQuery(getEntityClass());
        Root<Skill> skillRoot = cq.from(getEntityClass());
        cq.select(skillRoot);
        Join<Skill, Workspace> workspaceJoin = skillRoot.join("workspace");

        if (StringUtils.isNotBlank(filter.getValue())) {
            criterions.add(
                    cb.like(
                            cb.lower(skillRoot.<String>get("name")),
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
    public List<String> getExistingNames(List<String> skillNames,
            long workspaceId) {

        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<Skill> root = cq.from(getEntityClass());
        Join<Skill, Workspace> workspaceJoin = root.join("workspace");

        cq.multiselect(root.get("name"));

        Expression<String> exp = root.get("name");
        Predicate predicate = exp.in(skillNames);
        criterions.add(predicate);

        criterions.add(cb.equal(workspaceJoin.<Long>get("id"), workspaceId));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        return this.getEM().createQuery(cq).getResultList();
    }

    @Override
    protected Class<Skill> getEntityClass() {
        return Skill.class;
    }

}
