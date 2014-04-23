package org.profwell.security.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.auxiliary.UserFilter;
import org.profwell.security.model.Role;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;

public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public Workspace getWorkspace(long workspaceId) {
        return getEM().find(Workspace.class, workspaceId);
    }

    @Override
    public void saveWorkspace(Workspace workspace) {
        getEM().persist(workspace);
    }

    @Override
    public List<User> findAuthenticate(String username, String password) {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<User> cq = cb.createQuery(getEntityClass());

        Root<User> userRoot = cq.from(getEntityClass());
        cq.select(userRoot);

        ParameterExpression<String> peEmail = cb.parameter(String.class, "username");
        criterions.add(cb.equal(userRoot.<String>get("username"), peEmail));

        ParameterExpression<String> pePassword = cb.parameter(String.class, "password");
        criterions.add(cb.equal(userRoot.<String>get("password"), pePassword));

        cq.where(cb.and(criterions.toArray(new Predicate[0])));

        TypedQuery<User> query = getEM().createQuery(cq);
        query.setParameter("username", username);
        query.setParameter("password", password);

        return query.getResultList();
    }

    @Override
    public User findUserByUUID(String uuid) {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(getEntityClass());

        Root<User> userRoot = cq.from(getEntityClass());
        cq.select(userRoot);

        cq.where(cb.equal(userRoot.<String>get("uuid"), uuid));

        TypedQuery<User> query = getEM().createQuery(cq);

        return this.extractSingleFromList(query.getResultList());
    }

    @Override
    public List<User> getTimeoutedUsers() {
        CriteriaBuilder cb = getEM().getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(getEntityClass());

        Root<User> userRoot = cq.from(getEntityClass());
        cq.select(userRoot);

        cq.where(cb.not(cb.isNull(userRoot.<Timestamp>get("timeoutStamp"))));

        TypedQuery<User> query = getEM().createQuery(cq);

        return query.getResultList();
    }

    @Override
    public void setTimeoutForUser(String email, Date date) {
        Query q = getEM().createNativeQuery("UPDATE USER SET TIME_OUT_STAMP = :timeoutStamp WHERE USERNAME = :username");
        q.setParameter("timeoutStamp", date);
        q.setParameter("username", email);
        q.executeUpdate();
    }

    @Override
    public void unblockTimeoutedUsers(List<Long> usersIds) {
        Query q = getEM().createNativeQuery("UPDATE USER SET TIME_OUT_STAMP = NULL WHERE ID IN :userIds");
        q.setParameter("userIds", usersIds);
        q.executeUpdate();
    }

    @Override
    public List<User> listUser(UserFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<User> cq = cb.createQuery(getEntityClass());
        Root<User> userRoot = cq.from(getEntityClass());
        cq.select(userRoot);

        if (StringUtils.isNotBlank(filter.getName())) {
            criterions.add(
                    cb.or(
                            cb.like(userRoot.<String>get("firstName"), filter.getName() + "%"),
                            cb.like(userRoot.<String>get("lastName"), filter.getName() + "%")));
        }

        if (StringUtils.isNotBlank(filter.getUsername())) {
            criterions.add(cb.equal(userRoot.<String>get("username"),
                    filter.getUsername()));
        }

        if (filter.getRole() != null) {
            criterions.add(cb.equal(userRoot.<Role>get("role"),
                    filter.getRole()));
        }

        if (criterions.size() > 0) {
            cq.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return this.listPage(cq, filter);
    }

    @Override
    public long countUsers(String username) {
        CriteriaQuery<Long> cq = this.getEM().getCriteriaBuilder()
                .createQuery(Long.class);

        Root<User> root = cq.from(getEntityClass());

        cq.select(this.getEM().getCriteriaBuilder().count(root));

        cq.where(this.getEM().getCriteriaBuilder().equal(
                root.<Role>get("username"),
                username));

        return this.getEM().createQuery(cq).getSingleResult().longValue();
    }

    @Override
    public void changeUserPassword(Long userId, String newPassword) {
        Query q = getEM().createNativeQuery("UPDATE USER SET PASSWORD = :newPassword WHERE ID = :userId");
        q.setParameter("newPassword", newPassword);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }
}
