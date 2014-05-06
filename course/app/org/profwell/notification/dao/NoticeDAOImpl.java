package org.profwell.notification.dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;


public class NoticeDAOImpl extends GenericDAOImpl<Notice> implements NoticeDAO {

    @Override
    protected Class<Notice> getEntityClass() {
        return Notice.class;
    }
    
    @Override
    public List<Notice> loadMessages(User user) {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Notice> cq = cb.createQuery(getEntityClass());

        Root<Notice> noticeRoot = cq.from(getEntityClass());
        cq.select(noticeRoot);

        criterions.add(cb.equal(noticeRoot.<String>get("receiver"), user));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        TypedQuery<Notice> query = getEM().createQuery(cq);

        return query.getResultList();
    }
}
