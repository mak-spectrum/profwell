package org.profwell.security.dao;

import java.util.Date;
import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.security.auxiliary.UserFilter;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;

public interface UserDAO extends GenericDAO<User> {

    /**
     * Find and authenticate all {@link User}.
     */
    List<User> findAuthenticate(String email, String password);

    User findUserByUUID(String uuid);

    Workspace getWorkspace(long workspaceId);

    void saveWorkspace(Workspace workspace);

    List<User> getTimeoutedUsers();

    void setTimeoutForUser(String email, Date date);

    void unblockTimeoutedUsers(List<Long> usersIds);

    long countUsers(String username);

    List<User> listUser(UserFilter filter);

    void changeUserPassword(Long userId, String newPassword);
}
