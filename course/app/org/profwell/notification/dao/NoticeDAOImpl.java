package org.profwell.notification.dao;


import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.notification.auxiliary.NoticeFilter;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;


public class NoticeDAOImpl extends GenericDAOImpl<Notice> implements NoticeDAO {

    @Override
    protected Class<Notice> getEntityClass() {
        return Notice.class;
    }

    @Override
    public List<Notice> loadMessages(User user) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();

        CriteriaQuery<Notice> cq = cb.createQuery(this.getEntityClass());

        Root<Notice> noticeRoot = cq.from(this.getEntityClass());
        cq.select(noticeRoot);

        cq.where(cb.equal(noticeRoot.<String>get("receiver"), user));

        cq.orderBy(cb.desc(noticeRoot.get("id")));

        TypedQuery<Notice> query = this.getEM().createQuery(cq);

        return query.getResultList();
    }

    @Override
    public List<Notice> listNotice(NoticeFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();

        CriteriaQuery<Notice> cq = cb.createQuery(this.getEntityClass());

        Root<Notice> noticeRoot = cq.from(this.getEntityClass());
        cq.select(noticeRoot);

        cq.where(cb.like(noticeRoot.<String>get("text"), filter.getText()));

        cq.where(cb.equal(noticeRoot.<User>get("receiver").get("id"), filter.getWorkspaceId()));

        cq.orderBy(cb.desc(noticeRoot.get("id")));

        return this.listPage(cq, filter);
    }

}
